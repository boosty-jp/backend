package co.jp.wever.graphql.infrastructure.repository.plan;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

import co.jp.wever.graphql.domain.repository.plan.CreatePlanRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.edge.label.PlanToPlanElementEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.PlanToTagEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToPlanEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.property.PlanToPlanElementProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexType;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.PlanVertexProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.VertexCommonProperty;
import co.jp.wever.graphql.infrastructure.datamodel.PlanBaseEntity;
import co.jp.wever.graphql.infrastructure.datamodel.PlanElementEntity;

@Component
public class CreatePlanRepositoryImpl implements CreatePlanRepository {
    private final NeptuneClient neptuneClient;

    public CreatePlanRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    @Override
    public String createBase(String userId, PlanBaseEntity planBaseEntity) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        String planId = g.addV()
                         .property(VertexCommonProperty.Type.name(), VertexType.PLAN.name())
                         .property(PlanVertexProperty.TITLE.name(), planBaseEntity.getTitle())
                         .property(PlanVertexProperty.DESCRIPTION.name(), planBaseEntity.getDescription())
                         .property(PlanVertexProperty.IMAGE_URL.name(), planBaseEntity.getImageUrl())
                         .next()
                         .id()
                         .toString();

        g.V(planBaseEntity.getTagIds()).addE(PlanToTagEdge.RELATED.name()).from(g.V(planId)).next();
        g.V(userId).addE(UserToPlanEdge.DRAFT.name()).to(g.V(planId)).property("drafted", "today").next();

        return planId;
    }


    @Override
    public void createElements(String planId, List<PlanElementEntity> planElementEntities) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        IntStream.range(0, planElementEntities.size()).forEach(i -> {
            if (i == planElementEntities.size()) {
                g.V(planId)
                 .addE(PlanToPlanElementEdge.INCLUDE.name())
                 .to(g.V(planElementEntities.get(i).getTargetId()))
                 .property(PlanToPlanElementProperty.NUMBER, planElementEntities.get(i).getNumber())
                 .next();
            } else {
                g.V(planId)
                 .addE(PlanToPlanElementEdge.INCLUDE.name())
                 .to(g.V(planElementEntities.get(i).getTargetId()))
                 .property(PlanToPlanElementProperty.NUMBER, planElementEntities.get(i).getNumber());
            }
        });
    }
}
