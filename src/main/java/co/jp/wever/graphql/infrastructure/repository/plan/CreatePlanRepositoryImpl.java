package co.jp.wever.graphql.infrastructure.repository.plan;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.T;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

import co.jp.wever.graphql.domain.repository.plan.CreatePlanRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.edge.label.PlanToPlanElementEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToPlanEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.property.PlanToPlanElementProperty;
import co.jp.wever.graphql.infrastructure.constant.edge.property.UserToPlanProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.PlanVertexProperty;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanBaseEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanElementEntity;
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

        String planId = g.addV(VertexLabel.PLAN.getString())
                         .property(PlanVertexProperty.TITLE.getString(), "")
                         .property(PlanVertexProperty.DESCRIPTION.getString(), "")
                         .property(PlanVertexProperty.IMAGE_URL.getString(), "")
                         .property(PlanVertexProperty.CREATED_TIME.getString(), now)
                         .property(PlanVertexProperty.UPDATED_TIME.getString(), now)
                         .next()
                         .id()
                         .toString();

        g.V(userId)
         .addE(UserToPlanEdge.DRAFTED.getString())
         .property(T.id, EdgeIdCreator.userDraftedPlan(userId, planId))
         .to(g.V(planId))
         .property(UserToPlanProperty.DRAFTED_TIME.getString(), now)
         .next();

        return planId;
    }

    @Override
    public String createBase(String userId, PlanBaseEntity planBaseEntity) {
//        GraphTraversalSource g = neptuneClient.newTraversal();
//        String planId = g.addV(VertexLabel.PLAN.getString())
//                         .property(PlanVertexProperty.TITLE.getString(), planBaseEntity.getTitle())
//                         .property(PlanVertexProperty.DESCRIPTION.getString(), planBaseEntity.getDescription())
//                         .property(PlanVertexProperty.IMAGE_URL.getString(), planBaseEntity.getImageUrl())
//                         .next()
//                         .id()
//                         .toString();
//
//        g.V(planBaseEntity.getTagIds()).addE(PlanToTagEdge.RELATED.getString()).from(g.V(planId)).next();
//
//        long now = System.currentTimeMillis();
//
//        g.V(userId)
//         .addE(UserToPlanEdge.DRAFTED.getString())
//         .to(g.V(planId))
//         .property(UserToPlanProperty.DRAFTED_TIME.getString(), now)
//         .next();
//
//        //TODO: Algoliaにデータ追加する
//
//        return planId;
        return "";
    }


    @Override
    public void createElements(String planId, List<PlanElementEntity> planElementEntities) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        IntStream.range(0, planElementEntities.size()).forEach(i -> {
            if (i == planElementEntities.size()) {
                g.V(planId)
                 .addE(PlanToPlanElementEdge.INCLUDE.getString())
                 .to(g.V(planElementEntities.get(i).getId()))
                 .property(PlanToPlanElementProperty.NUMBER, planElementEntities.get(i).getNumber())
                 .next();
            } else {
                g.V(planId)
                 .addE(PlanToPlanElementEdge.INCLUDE.getString())
                 .to(g.V(planElementEntities.get(i).getId()))
                 .property(PlanToPlanElementProperty.NUMBER, planElementEntities.get(i).getNumber());
            }
        });
    }
}
