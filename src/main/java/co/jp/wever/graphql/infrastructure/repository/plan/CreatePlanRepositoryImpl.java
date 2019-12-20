package co.jp.wever.graphql.infrastructure.repository.plan;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.T;
import org.springframework.stereotype.Component;

import co.jp.wever.graphql.domain.repository.plan.CreatePlanRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.PlanVertexProperty;
import co.jp.wever.graphql.infrastructure.util.EdgeIdCreator;

@Component
public class CreatePlanRepositoryImpl implements CreatePlanRepository {
    private final NeptuneClient neptuneClient;

    public CreatePlanRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    @Override
    public String initOne(String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        String planId = g.addV(VertexLabel.COURSE.getString())
                         .property(PlanVertexProperty.TITLE.getString(), "")
                         .property(PlanVertexProperty.DESCRIPTION.getString(), "")
                         .property(PlanVertexProperty.LIKED.getString(), 0)
                         .property(PlanVertexProperty.LEARNED.getString(), 0)
                         .property(PlanVertexProperty.LEARNING.getString(), 0)
                         .property(PlanVertexProperty.IMAGE_URL.getString(), "")
                         .property(PlanVertexProperty.CREATED_TIME.getString(), now)
                         .property(PlanVertexProperty.UPDATED_TIME.getString(), now)
                         .next()
                         .id()
                         .toString();

        g.V(userId)
         .addE(UserToPlanEdge.DRAFTED.getString())
         .property(T.id, EdgeIdCreator.createId(userId, planId, UserToPlanEdge.DRAFTED.getString()))
         .to(g.V(planId))
         .property(UserToPlanProperty.CREATED_TIME.getString(), now)
         .property(UserToPlanProperty.UPDATED_TIME.getString(), now)
         .next();

        return planId;
    }
}
