package co.jp.wever.graphql.infrastructure.repository.plan;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.springframework.stereotype.Component;

import java.util.List;

import co.jp.wever.graphql.domain.repository.plan.UpdatePlanRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.edge.label.PlanToTagEdge;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.PlanVertexProperty;
import co.jp.wever.graphql.infrastructure.datamodel.PlanBaseEntity;
import co.jp.wever.graphql.infrastructure.datamodel.PlanElementEntity;

import static org.apache.tinkerpop.gremlin.structure.VertexProperty.Cardinality.single;

@Component
public class UpdatePlanRepositoryImpl implements UpdatePlanRepository {
    private final NeptuneClient neptuneClient;

    public UpdatePlanRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    @Override
    public void updateBase(String planId, PlanBaseEntity planBaseEntity) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        g.V(planId)
         .property(single, PlanVertexProperty.TITLE.name(), planBaseEntity.getTitle())
         .property(single, PlanVertexProperty.DESCRIPTION.name(), planBaseEntity.getDescription())
         .property(single, PlanVertexProperty.IMAGE_URL.name(), planBaseEntity.getImageUrl())
         .next();

        // タグを全消し
        g.V(planId).outE(PlanToTagEdge.RELATED.name()).drop();

        // タグを再追加
        g.V(planBaseEntity.getTagIds()).addE(PlanToTagEdge.RELATED.name()).from(g.V(planId)).next();
    }

    @Override
    public void updateElements(String planId, String userId, List<PlanElementEntity> planElementEntities) {
    }

    @Override
    public void publishOne(String planId, String userId) {
    }

    @Override
    public void draftOne(String planId, String userId) {
    }

    @Override
    public void startOne(String planId, String userId) {
    }

    @Override
    public void stopOne(String planId, String userId) {
    }
}
