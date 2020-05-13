package jp.boosty.graphql.infrastructure.repository.user;

import jp.boosty.graphql.infrastructure.constant.edge.EdgeLabel;
import jp.boosty.graphql.infrastructure.constant.vertex.property.DateProperty;

import org.apache.tinkerpop.gremlin.process.traversal.Order;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.WithOptions;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jp.boosty.graphql.domain.repository.user.UserQueryRepository;
import jp.boosty.graphql.infrastructure.connector.NeptuneClient;
import jp.boosty.graphql.infrastructure.constant.vertex.label.VertexLabel;
import jp.boosty.graphql.infrastructure.converter.entity.user.OrderHistoryEntityConverter;
import jp.boosty.graphql.infrastructure.converter.entity.user.UserEntityConverter;
import jp.boosty.graphql.infrastructure.datamodel.user.OrderHistoriesEntity;
import jp.boosty.graphql.infrastructure.datamodel.user.UserEntity;

import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.coalesce;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.inE;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.values;

@Component
public class UserQueryRepositoryImpl implements UserQueryRepository {
    private final NeptuneClient neptuneClient;

    public UserQueryRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    @Override
    public UserEntity findOne(String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        Map<Object, Object> allResult =
            g.V(userId).hasLabel(VertexLabel.USER.getString()).valueMap().with(WithOptions.tokens).next();

        return UserEntityConverter.toUserEntityFromVertex(allResult);
    }

    @Override
    public OrderHistoriesEntity findOrderHistories(int page, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        List<Map<String, Object>> allResult = g.V(userId)
                                               .hasLabel(VertexLabel.USER.getString())
                                               .out(EdgeLabel.PURCHASE.getString())
                                               .hasLabel(VertexLabel.BOOK.getString())
                                               .order()
                                               .by(coalesce(inE(EdgeLabel.PURCHASE.getString()).values(DateProperty.CREATE_TIME
                                                                                                           .getString()),
                                                            values(DateProperty.CREATE_TIME.getString())), Order.desc)
                                               .range(24 * (page - 1), 24 * page)
                                               .project("base", "edge", "author")
                                               .by(__.valueMap().with(WithOptions.tokens))
                                               .by(__.inE(EdgeLabel.PURCHASE.getString()).valueMap())
                                               .by(__.in(EdgeLabel.PUBLISH.getString(),
                                                         EdgeLabel.SUSPEND.getString(),
                                                         EdgeLabel.DRAFT.getString())
                                                     .hasLabel(VertexLabel.USER.getString())
                                                     .valueMap()
                                                     .with(WithOptions.tokens))
                                               .toList();

        long sumCount =
            g.V(userId).hasLabel(VertexLabel.USER.getString()).outE(EdgeLabel.PURCHASE.getString()).count().next();

        return OrderHistoriesEntity.builder()
                                   .orderHistoryEntityList(allResult.stream()
                                                                    .map(r -> OrderHistoryEntityConverter.toOrderHistoryEntity(
                                                                        r))
                                                                    .collect(Collectors.toList()))
                                   .sumCount(sumCount)
                                   .build();
    }
}
