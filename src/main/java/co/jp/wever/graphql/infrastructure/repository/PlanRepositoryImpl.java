package co.jp.wever.graphql.infrastructure.repository;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.WithOptions;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import co.jp.wever.graphql.domain.repository.PlanRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.UserPlanEdge;
import co.jp.wever.graphql.infrastructure.constant.Vertex;
import co.jp.wever.graphql.infrastructure.converter.entity.PlanEntityConverter;
import co.jp.wever.graphql.infrastructure.converter.entity.PlansEntityConverter;
import co.jp.wever.graphql.infrastructure.datamodel.PlanElementEntity;
import co.jp.wever.graphql.infrastructure.datamodel.PlanEntity;

@Component
public class PlanRepositoryImpl implements PlanRepository {

    private final NeptuneClient neptuneClient;

    public PlanRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    @Override
    public PlanEntity findOne(String planId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        Map<Object, Object> result = g.V(planId).valueMap().with(WithOptions.tokens).next();
        return PlanEntityConverter.toPlan(result);
    }

    @Override
    public List<PlanEntity> findAll(String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<Object, Object>> result = g.V(userId).out().has("type", Vertex.PLAN.name()).valueMap().with(WithOptions.tokens).toList();

        return PlansEntityConverter.toPlans(result);
    }

    @Override
    public List<PlanEntity> findAllPublished(String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<Object, Object>> result = g.V(userId).out(UserPlanEdge.PUBLISH.name()).has("type", Vertex.PLAN.name()).valueMap().with(WithOptions.tokens).toList();

        return PlansEntityConverter.toPlans(result);
    }

    @Override
    public List<PlanEntity> findAllDrafted(String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<Object, Object>> result = g.V(userId).out(UserPlanEdge.DRAFT.name()).has("type", Vertex.PLAN.name()).valueMap().with(WithOptions.tokens).toList();

        return PlansEntityConverter.toPlans(result);
    }

    @Override
    public List<PlanEntity> findAllLiked(String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<Object, Object>> result = g.V(userId).out(UserPlanEdge.LIKE.name()).has("type", Vertex.PLAN.name()).valueMap().with(WithOptions.tokens).toList();

        return PlansEntityConverter.toPlans(result);
    }

    @Override
    public List<PlanEntity> findAllLearning(String userId) {

        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<Object, Object>> result = g.V(userId).out(UserPlanEdge.LEARNING.name()).has("type", Vertex.PLAN.name()).valueMap().with(WithOptions.tokens).toList();

        return PlansEntityConverter.toPlans(result);
    }

    @Override
    public List<PlanEntity> findAllLearned(String userId) {

        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<Object, Object>> result = g.V(userId).out(UserPlanEdge.LEARNED.name()).has("type", Vertex.PLAN.name()).valueMap().with(WithOptions.tokens).toList();

        return PlansEntityConverter.toPlans(result);
    }

    @Override
    public List<PlanEntity> findFamous(String userId) {

        //TODO: 未実装
        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<Object, Object>> result = g.V(userId).out(UserPlanEdge.PUBLISH.name()).has("type", Vertex.PLAN.name()).valueMap().with(WithOptions.tokens).toList();

        return PlansEntityConverter.toPlans(result);
    }

    @Override
    public List<PlanEntity> findRelated(String userId) {

        //TODO: 未実装
        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<Object, Object>> result = g.V(userId).out(UserPlanEdge.PUBLISH.name()).has("type", Vertex.PLAN.name()).valueMap().with(WithOptions.tokens).toList();

        return PlansEntityConverter.toPlans(result);
    }

    @Override
    public boolean isPublishedPlanElement(String planElementId) {
        return true;
    }

    @Override
    public String initOne(String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        String planId = g.addV().property("type", Vertex.PLAN.name()).next().id().toString();
        g.V(userId).addE(UserPlanEdge.DRAFT.name()).to(g.V(planId)).property("drafted", "today").next();

        return planId;
    }

    @Override
    public String addOne(String planId, String userId, PlanElementEntity addElementEntity) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        return "";
    }

    @Override
    public PlanEntity updateOne(String planId, String title, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        g.V(planId).property(VertexProperty.Cardinality.single, "title", title).next();

        return PlanEntity.builder().build();
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
