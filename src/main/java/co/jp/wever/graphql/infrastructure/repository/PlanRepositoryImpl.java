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
import co.jp.wever.graphql.infrastructure.converter.PlanConverter;
import co.jp.wever.graphql.infrastructure.converter.PlansConverter;
import co.jp.wever.graphql.infrastructure.datamodel.Plan;

@Component
public class PlanRepositoryImpl implements PlanRepository {

    private final NeptuneClient neptuneClient;

    public PlanRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    @Override
    public Plan findOne(String planId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        Map<Object, Object> result = g.V(planId).valueMap().with(WithOptions.tokens).next();
        return PlanConverter.toPlan(result);
    }

    @Override
    public List<Plan> findAll(String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<Object, Object>> result = g.V(userId).out().has("type", Vertex.PLAN.name()).valueMap().with(WithOptions.tokens).toList();

        return PlansConverter.toPlans(result);
    }

    @Override
    public List<Plan> findAllPublished(String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<Object, Object>> result = g.V(userId).out(UserPlanEdge.PUBLISH.name()).has("type", Vertex.PLAN.name()).valueMap().with(WithOptions.tokens).toList();

        return PlansConverter.toPlans(result);
    }

    @Override
    public List<Plan> findAllDrafted(String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<Object, Object>> result = g.V(userId).out(UserPlanEdge.DRAFT.name()).has("type", Vertex.PLAN.name()).valueMap().with(WithOptions.tokens).toList();

        return PlansConverter.toPlans(result);
    }

    @Override
    public List<Plan> findAllLiked(String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<Object, Object>> result = g.V(userId).out(UserPlanEdge.LIKE.name()).has("type", Vertex.PLAN.name()).valueMap().with(WithOptions.tokens).toList();

        return PlansConverter.toPlans(result);
    }

    @Override
    public List<Plan> findAllLearning(String userId) {

        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<Object, Object>> result = g.V(userId).out(UserPlanEdge.LEARNING.name()).has("type", Vertex.PLAN.name()).valueMap().with(WithOptions.tokens).toList();

        return PlansConverter.toPlans(result);
    }

    @Override
    public List<Plan> findAllLearned(String userId) {

        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<Object, Object>> result = g.V(userId).out(UserPlanEdge.LEARNED.name()).has("type", Vertex.PLAN.name()).valueMap().with(WithOptions.tokens).toList();

        return PlansConverter.toPlans(result);
    }

    @Override
    public List<Plan> findFamous(String userId) {

        //TODO: 未実装
        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<Object, Object>> result = g.V(userId).out(UserPlanEdge.PUBLISH.name()).has("type", Vertex.PLAN.name()).valueMap().with(WithOptions.tokens).toList();

        return PlansConverter.toPlans(result);
    }

    @Override
    public List<Plan> findRelated(String userId) {

        //TODO: 未実装
        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<Object, Object>> result = g.V(userId).out(UserPlanEdge.PUBLISH.name()).has("type", Vertex.PLAN.name()).valueMap().with(WithOptions.tokens).toList();

        return PlansConverter.toPlans(result);
    }

    @Override
    public String initOne(String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        String planId = g.addV().property("type", Vertex.PLAN.name()).next().id().toString();
        g.V(userId).addE(UserPlanEdge.DRAFT.name()).to(g.V(planId)).property("drafted", "today").next();

        return planId;
    }

    @Override
    public String addOne(String id) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        return "";
    }

    @Override
    public Plan updateOne(String planId, String title, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        g.V(planId).property(VertexProperty.Cardinality.single, "title", title).next();

        return Plan.builder().build();
    }

    @Override
    public Plan deleteOne(String id) {
        return Plan.builder().build();
    }

    @Override
    public Plan publishOne(String id) {
        return Plan.builder().build();
    }

    @Override
    public Plan draftOne(String id) {
        return Plan.builder().build();
    }

    @Override
    public Plan startOne(String id) {
        return Plan.builder().build();
    }
}
