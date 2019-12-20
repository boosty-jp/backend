package co.jp.wever.graphql.infrastructure.repository.course;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.T;
import org.springframework.stereotype.Component;

import co.jp.wever.graphql.domain.repository.course.DeletePlanRepository;
import co.jp.wever.graphql.infrastructure.connector.AlgoliaClient;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.util.EdgeIdCreator;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.outV;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.unfold;

@Component
public class DeletePlanRepositoryImpl implements DeletePlanRepository {
    private final NeptuneClient neptuneClient;
    private final AlgoliaClient algoliaClient;

    public DeletePlanRepositoryImpl(
        NeptuneClient neptuneClient, AlgoliaClient algoliaClient) {
        this.neptuneClient = neptuneClient;
        this.algoliaClient = algoliaClient;
    }

    @Override
    public void deleteOne(String planId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        g.V(planId)
         .inE(UserToPlanEdge.PUBLISHED.getString(), UserToPlanEdge.DRAFTED.getString())
         .where(outV().hasId(userId))
         .drop()
         .iterate();

        g.E(EdgeIdCreator.createId(userId, planId, UserToPlanEdge.DELETED.getString()))
         .fold()
         .coalesce(unfold(),
                   g.V(userId)
                    .addE(UserToPlanEdge.DELETED.getString())
                    .to(g.V(planId))
                    .property(T.id, EdgeIdCreator.createId(userId, planId, UserToPlanEdge.DELETED.getString()))
                    .property(UserToPlanProperty.CREATED_TIME.getString(), now)
                    .property(UserToPlanProperty.UPDATED_TIME.getString(), now))
         .next();

        algoliaClient.getCourseIndex().deleteObjectAsync(planId);
    }
}
