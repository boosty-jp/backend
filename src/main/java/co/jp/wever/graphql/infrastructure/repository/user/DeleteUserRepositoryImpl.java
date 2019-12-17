package co.jp.wever.graphql.infrastructure.repository.user;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.springframework.stereotype.Component;

import co.jp.wever.graphql.domain.repository.user.DeleteUserRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.UserVertexProperty;

import static org.apache.tinkerpop.gremlin.structure.VertexProperty.Cardinality.single;

@Component
public class DeleteUserRepositoryImpl implements DeleteUserRepository {
    private final NeptuneClient neptuneClient;

    public DeleteUserRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    @Override
    public void deleteUser(String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        g.V(userId)
         .property(single, UserVertexProperty.DELETED.getString(), true)
         .property(single, UserVertexProperty.UPDATED_TIME.getString(), now)
         .next();
    }
}
