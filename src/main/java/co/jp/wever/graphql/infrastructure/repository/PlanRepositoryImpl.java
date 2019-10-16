package co.jp.wever.graphql.infrastructure.repository;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.WithOptions;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import co.jp.wever.graphql.domain.repository.PlanRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.edge.label.PlanToPlanElementEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.PlanToTagEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToPlanEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.property.PlanToPlanElementProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.PlanVertexProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.VertexCommonProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexType;
import co.jp.wever.graphql.infrastructure.converter.entity.PlanBaseEntityConverter;
import co.jp.wever.graphql.infrastructure.converter.entity.PlanEntityConverter;
import co.jp.wever.graphql.infrastructure.converter.entity.PlansEntityConverter;
import co.jp.wever.graphql.infrastructure.datamodel.PlanBaseEntity;
import co.jp.wever.graphql.infrastructure.datamodel.PlanElementEntity;
import co.jp.wever.graphql.infrastructure.datamodel.PlanEntity;

import static org.apache.tinkerpop.gremlin.structure.VertexProperty.Cardinality.single;

@Component
public class PlanRepositoryImpl implements PlanRepository {

    private final NeptuneClient neptuneClient;

    public PlanRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    @Override
    public PlanEntity findOne(String planId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        Map<Object, Object> planResult = g.V(planId).valueMap().with(WithOptions.tokens).next();

        Map<Object, Object> tagResult =
            g.V(planId).out(PlanToTagEdge.RELATED.name()).valueMap().with(WithOptions.tokens).next();

        Map<Object, Object> userResult = g.V(planId).in().valueMap().with(WithOptions.tokens).next();

        Map<Object, Object> statusResult =
            g.V(planId).out(PlanToTagEdge.RELATED.name()).valueMap().with(WithOptions.tokens).next();

        Map<Object, Object> elementsResult =
            g.V(planId).out(PlanToTagEdge.RELATED.name()).valueMap().with(WithOptions.tokens).next();
        return PlanEntityConverter.toPlan(planResult, tagResult, userResult, statusResult, elementsResult);
    }


    @Override
    public List<PlanEntity> findAll(String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<Object, Object>> result =
            g.V(userId).out().has("type", VertexType.PLAN.name()).valueMap().with(WithOptions.tokens).toList();

        return PlansEntityConverter.toPlans(result);
    }

    @Override
    public List<PlanEntity> findAllPublished(String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<Object, Object>> result = g.V(userId)
                                            .out(UserToPlanEdge.PUBLISH.name())
                                            .has("type", VertexType.PLAN.name())
                                            .valueMap()
                                            .with(WithOptions.tokens)
                                            .toList();

        return PlansEntityConverter.toPlans(result);
    }

    @Override
    public List<PlanEntity> findAllDrafted(String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<Object, Object>> result = g.V(userId)
                                            .out(UserToPlanEdge.DRAFT.name())
                                            .has("type", VertexType.PLAN.name())
                                            .valueMap()
                                            .with(WithOptions.tokens)
                                            .toList();

        return PlansEntityConverter.toPlans(result);
    }

    @Override
    public List<PlanEntity> findAllLiked(String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<Object, Object>> result = g.V(userId)
                                            .out(UserToPlanEdge.LIKE.name())
                                            .has("type", VertexType.PLAN.name())
                                            .valueMap()
                                            .with(WithOptions.tokens)
                                            .toList();

        return PlansEntityConverter.toPlans(result);
    }

    @Override
    public List<PlanEntity> findAllLearning(String userId) {

        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<Object, Object>> result = g.V(userId)
                                            .out(UserToPlanEdge.LEARNING.name())
                                            .has("type", VertexType.PLAN.name())
                                            .valueMap()
                                            .with(WithOptions.tokens)
                                            .toList();

        return PlansEntityConverter.toPlans(result);
    }

    @Override
    public List<PlanEntity> findAllLearned(String userId) {

        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<Object, Object>> result = g.V(userId)
                                            .out(UserToPlanEdge.LEARNED.name())
                                            .has("type", VertexType.PLAN.name())
                                            .valueMap()
                                            .with(WithOptions.tokens)
                                            .toList();

        return PlansEntityConverter.toPlans(result);
    }

    @Override
    public List<PlanEntity> findFamous(String userId) {

        //TODO: 未実装
        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<Object, Object>> result = g.V(userId)
                                            .out(UserToPlanEdge.PUBLISH.name())
                                            .has("type", VertexType.PLAN.name())
                                            .valueMap()
                                            .with(WithOptions.tokens)
                                            .toList();

        return PlansEntityConverter.toPlans(result);
    }

    @Override
    public List<PlanEntity> findRelated(String userId) {

        //TODO: 未実装
        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<Object, Object>> result = g.V(userId)
                                            .out(UserToPlanEdge.PUBLISH.name())
                                            .has("type", VertexType.PLAN.name())
                                            .valueMap()
                                            .with(WithOptions.tokens)
                                            .toList();

        return PlansEntityConverter.toPlans(result);
    }

    @Override
    public PlanBaseEntity findBase(String planId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        Map<Object, Object> planResult = g.V(planId).valueMap().with(WithOptions.tokens).next();

        Map<Object, Object> tagResult =
            g.V(planId).out(PlanToTagEdge.RELATED.name()).valueMap().with(WithOptions.tokens).next();

        Map<Object, Object> userResult = g.V(planId).in().valueMap().with(WithOptions.tokens).next();

        Map<Object, Object> statusResult =
            g.V(planId).out(PlanToTagEdge.RELATED.name()).valueMap().with(WithOptions.tokens).next();

        return PlanBaseEntityConverter.toPlanBaseEntity(planResult, tagResult, userResult, statusResult);
    }

    @Override
    public List<String> findPublishedPlanElementIds(List<String> planElementIds) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        Map<Object, Object> results = g.V(planElementIds).valueMap().with(WithOptions.tokens).next();
        // IDだけ取得する
        //TODO: 型がどうなってるか要検証
        //        return PlanEntityConverter.toPlan(result);

        return null;
    }

    // TODO: 未実装
    @Override
    public boolean isPublishedPlanElement(String planElementId) {
        return true;
    }


    // TODO: 未実装
    @Override
    public boolean isPublishedPlan(String planId) {
        return true;
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

    @Override
    public void updateElements(String planId, List<PlanElementEntity> planElementEntities) {
    }

    @Override
    public PlanEntity deleteOne(String planId, String userId) {
        return PlanEntity.builder().build();
    }

    @Override
    public PlanEntity publishOne(String planId, String userId) {
        return PlanEntity.builder().build();
    }

    @Override
    public PlanEntity draftOne(String planId, String userId) {
        return PlanEntity.builder().build();
    }

    @Override
    public PlanEntity startOne(String planId, String userId) {
        return PlanEntity.builder().build();
    }
}
