package co.jp.wever.graphql.infrastructure.repository.plan;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.repository.plan.UpdatePlanRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.edge.label.PlanToTagEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToPlanEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.property.UserToPlanProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.PlanVertexProperty;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanBaseEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanElementEntity;

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
         .property(single, PlanVertexProperty.TITLE.getString(), planBaseEntity.getTitle())
         .property(single, PlanVertexProperty.DESCRIPTION.getString(), planBaseEntity.getDescription())
         .property(single, PlanVertexProperty.IMAGE_URL.getString(), planBaseEntity.getImageUrl())
         .next();

        // タグの張替え
        // TODO:タグの変更がないときは更新しないようにしたい
        g.V(planId).outE(PlanToTagEdge.RELATED.getString()).drop();
        g.V(planBaseEntity.getTagIds()).addE(PlanToTagEdge.RELATED.getString()).from(g.V(planId)).next();

        // TODO: Algolia更新
    }

    @Override
    public void updateElements(String planId, List<PlanElementEntity> planElementEntities) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        // エレメントの総張替え
        // TODO:変更がないエレメントは更新しないようにしたい
        g.V(planId)
         .out(PlanToTagEdge.RELATED.getString())
         .hasLabel(VertexLabel.ARTICLE.getString(), VertexLabel.PLAN.getString())
         .drop();

        List<String> elementIds = planElementEntities.stream().map(e -> e.getTargetId()).collect(Collectors.toList());
        g.V(elementIds).addE(PlanToTagEdge.RELATED.getString()).from(g.V(planId)).next();
    }

    @Override
    public void publishOne(String planId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        g.V(planId).inE(UserToPlanEdge.DRAFTED.getString(), UserToPlanEdge.DELETED.getString()).from(g.V(userId)).drop();

        long now = System.currentTimeMillis() / 1000L;
        g.V(userId)
         .addE(UserToPlanEdge.PUBLISHED.getString())
         .to(g.V(planId))
         .property(UserToPlanProperty.PUBLISHED_TIME.getString(), now)
         .next();

        // TODO: Algoliaに追加
    }

    @Override
    public void draftOne(String planId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        g.V(planId).inE(UserToPlanEdge.PUBLISHED.getString(), UserToPlanEdge.DELETED.getString()).from(g.V(userId)).drop();

        long now = System.currentTimeMillis() / 1000L;
        g.V(userId)
         .addE(UserToPlanEdge.DRAFTED.getString())
         .to(g.V(planId))
         .property(UserToPlanProperty.DRAFTED_TIME.getString(), now)
         .next();
        // TODO: Algoliaから削除
    }

    @Override
    public void startOne(String planId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        long now = System.currentTimeMillis() / 1000L;
        g.V(userId)
         .addE(UserToPlanEdge.LEARNING.getString())
         .to(g.V(planId))
         .property(UserToPlanProperty.LEARN_STARTED_TIME.getString(), now)
         .next();
    }

    @Override
    public void finishOne(String planId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        long learnStartTime;
        try {
            // 学習開始時刻を取得する
            Map<String, Object> edgeResult =
                g.V(planId).inE(UserToPlanEdge.LEARNING.getString()).from(g.V(userId)).propertyMap().next();
            learnStartTime = (long) edgeResult.get(UserToPlanProperty.LEARN_STARTED_TIME.getString());
            g.V(planId).inE(UserToPlanEdge.LEARNING.getString()).from(g.V(userId)).drop();
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }

        long now = System.currentTimeMillis() / 1000L;
        g.V(userId)
         .addE(UserToPlanEdge.LEARNED.getString())
         .to(g.V(planId))
         .property(UserToPlanProperty.LEARN_FINISHED_TIME.getString(), now)
         .property(UserToPlanProperty.LEARN_STARTED_TIME.getString(), learnStartTime)
         .next();
    }

    @Override
    public void stopOne(String planId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        g.V(planId).inE(UserToPlanEdge.LEARNING.getString(), UserToPlanEdge.LEARNED.getString()).from(g.V(userId)).drop();
    }
}
