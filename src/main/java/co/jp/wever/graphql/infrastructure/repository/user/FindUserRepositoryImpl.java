package co.jp.wever.graphql.infrastructure.repository.user;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.WithOptions;
import org.springframework.stereotype.Component;

import java.util.Map;

import co.jp.wever.graphql.domain.repository.user.FindUserRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToTagEdge;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.converter.entity.user.UserEntityConverter;
import co.jp.wever.graphql.infrastructure.datamodel.user.UserEntity;


@Component
public class FindUserRepositoryImpl implements FindUserRepository {

    private final NeptuneClient neptuneClient;

    public FindUserRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    @Override
    public UserEntity findOne(String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        Map<String, Object> allResult = g.V(userId)
                                         .hasLabel(VertexLabel.USER.getString())
                                         .project("user", "tags")
                                         .by(__.valueMap().with(WithOptions.tokens))
                                         //                                         .by(__.out(UserToTagEdge.RELATED.getString())
                                         //                                               .values(TagVertexProperty.NAME.getString())
                                         //                                               .fold())
                                         .by(__.out(UserToTagEdge.RELATED.getString())
                                               .hasLabel(VertexLabel.TAG.getString())
                                               .valueMap()
                                               .with(WithOptions.tokens)
                                               .fold())
                                         .next();

        UserEntity res = UserEntityConverter.toUserEntityByVertex(allResult);
        return res;
    }
}
