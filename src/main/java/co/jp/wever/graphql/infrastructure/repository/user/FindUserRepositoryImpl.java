package co.jp.wever.graphql.infrastructure.repository.user;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.WithOptions;
import org.springframework.stereotype.Component;

import java.util.Map;

import co.jp.wever.graphql.domain.repository.user.FindUserRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
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

        Map<Object, Object> result = g.V(userId).valueMap().with(WithOptions.tokens).next();
        System.out.println(result);

        return UserEntityConverter.toUserEntity(result);
    }
}
