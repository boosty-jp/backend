package co.jp.wever.graphql.infrastructure.repository.user;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.springframework.stereotype.Component;

import co.jp.wever.graphql.domain.repository.user.DeleteUserRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;

@Component
public class DeleteUserRepositoryImpl implements DeleteUserRepository {
    private final NeptuneClient neptuneClient;

    public DeleteUserRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    @Override
    public void deleteUser(String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        g.V(userId).drop().iterate();
    }
}
