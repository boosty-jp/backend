package co.jp.wever.graphql.infrastructure.repository.plan;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.WithOptions;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import co.jp.wever.graphql.domain.repository.plan.FindPlanRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.edge.label.PlanToTagEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToPlanEdge;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexType;
import co.jp.wever.graphql.infrastructure.converter.entity.PlanBaseEntityConverter;
import co.jp.wever.graphql.infrastructure.converter.entity.PlansEntityConverter;
import co.jp.wever.graphql.infrastructure.datamodel.PlanBaseEntity;
import co.jp.wever.graphql.infrastructure.datamodel.PlanEntity;

@Component
public class FindPlanRepositoryImpl implements FindPlanRepository {
    private final NeptuneClient neptuneClient;

    public FindPlanRepositoryImpl(NeptuneClient neptuneClient) {
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

        //TODO Neptuneの動きを見て↑を最適化する
        return PlanEntity.builder().build();
//        return PlanEntityConverter.toPlan(planResult, tagResult, userResult, statusResult, elementsResult);
    }


        @Override
        public List<PlanEntity> findAll(String userId) {
            GraphTraversalSource g = neptuneClient.newTraversal();
            List<Map<Object, Object>> result =
                g.V(userId).out().has("type", VertexType.PLAN.name()).valueMap().with(WithOptions.tokens).toList();

            //TODO: あとで実装
//            return result.stream().map(e -> PlanBaseEntityConverter.toPlanBaseEntity(e)).collect(Collectors.toList());
            return null;
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
}
