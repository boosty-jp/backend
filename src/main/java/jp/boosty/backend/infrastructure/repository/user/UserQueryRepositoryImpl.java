package jp.boosty.backend.infrastructure.repository.user;

import jp.boosty.backend.domain.GraphQLCustomException;
import jp.boosty.backend.infrastructure.constant.GraphQLErrorMessage;
import jp.boosty.backend.infrastructure.constant.edge.EdgeLabel;
import jp.boosty.backend.infrastructure.constant.vertex.property.DateProperty;

import org.apache.tinkerpop.gremlin.process.traversal.Order;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.WithOptions;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jp.boosty.backend.domain.repository.user.UserQueryRepository;
import jp.boosty.backend.infrastructure.connector.NeptuneClient;
import jp.boosty.backend.infrastructure.constant.vertex.label.VertexLabel;
import jp.boosty.backend.infrastructure.converter.entity.user.OrderHistoryEntityConverter;
import jp.boosty.backend.infrastructure.converter.entity.user.UserEntityConverter;
import jp.boosty.backend.infrastructure.datamodel.user.OrderHistoriesEntity;
import jp.boosty.backend.infrastructure.datamodel.user.UserEntity;

import lombok.extern.slf4j.Slf4j;

import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.coalesce;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.inE;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.outV;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.values;

@Slf4j
@Component
public class UserQueryRepositoryImpl implements UserQueryRepository {
    private final NeptuneClient neptuneClient;

    public UserQueryRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    @Override
    public UserEntity findOne(String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        Map<Object, Object> allResult;
        try {
            allResult = g.V(userId).hasLabel(VertexLabel.USER.getString()).valueMap().with(WithOptions.tokens).next();
        } catch (Exception e) {
            if (e.getMessage().equals("null")) {
                // 会員登録しようとした場合にこのルートを通ることがある(頻発するのでログは出さない)
                throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                                 GraphQLErrorMessage.USER_NOT_FOUND.getString());
            } else {
                log.error("findOne error: {} {}", userId, e.getMessage());
                throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                                 GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
            }
        }
        return UserEntityConverter.toUserEntityFromVertex(allResult);
    }

    @Override
    public OrderHistoriesEntity findOrderHistories(int page, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        List<Map<String, Object>> allResult;
        long sumCount;
        try {
            allResult = g.V(userId)
                         .hasLabel(VertexLabel.USER.getString())
                         .out(EdgeLabel.PURCHASE.getString())
                         .hasLabel(VertexLabel.BOOK.getString())
                         .order()
                         .by(coalesce(inE(EdgeLabel.PURCHASE.getString()).values(DateProperty.CREATE_TIME.getString()),
                                      values(DateProperty.CREATE_TIME.getString())), Order.desc)
                         .range(24 * (page - 1), 24 * page)
                         .project("base", "edge", "author")
                         .by(__.valueMap().with(WithOptions.tokens))
                         .by(__.inE(EdgeLabel.PURCHASE.getString())
                               .where(outV().hasId(userId).hasLabel(VertexLabel.USER.getString()))
                               .limit(1)
                               .valueMap())
                         .by(__.in(EdgeLabel.PUBLISH.getString(),
                                   EdgeLabel.SUSPEND.getString(),
                                   EdgeLabel.DRAFT.getString())
                               .hasLabel(VertexLabel.USER.getString())
                               .valueMap()
                               .with(WithOptions.tokens))
                         .toList();

            sumCount =
                g.V(userId).hasLabel(VertexLabel.USER.getString()).outE(EdgeLabel.PURCHASE.getString()).count().next();
        } catch (Exception e) {
            log.error("findOrderHistories error: {} {} {}", page, userId, e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }

        return OrderHistoriesEntity.builder()
                                   .orderHistoryEntityList(allResult.stream()
                                                                    .map(r -> OrderHistoryEntityConverter.toOrderHistoryEntity(
                                                                        r))
                                                                    .collect(Collectors.toList()))
                                   .sumCount(sumCount)
                                   .build();
    }
}
