package co.jp.wever.graphql.infrastructure.repository.user;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.WithOptions;
import org.springframework.stereotype.Component;

import java.util.Map;

import co.jp.wever.graphql.domain.repository.user.UserQueryRepository;
import co.jp.wever.graphql.infrastructure.connector.AlgoliaClient;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.converter.entity.user.UserEntityConverter;
import co.jp.wever.graphql.infrastructure.datamodel.user.UserEntity;

@Component
public class UserQueryRepositoryImpl implements UserQueryRepository {
    private final NeptuneClient neptuneClient;
    private final AlgoliaClient algoliaClient;

    public UserQueryRepositoryImpl(
        NeptuneClient neptuneClient, AlgoliaClient algoliaClient) {
        this.neptuneClient = neptuneClient;
        this.algoliaClient = algoliaClient;
    }

    @Override
    public UserEntity findOne(String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        Map<Object, Object> allResult = g.V(userId).valueMap().with(WithOptions.tokens).next();

        return UserEntityConverter.toUserEntityFromVertex(allResult);
    }
}
