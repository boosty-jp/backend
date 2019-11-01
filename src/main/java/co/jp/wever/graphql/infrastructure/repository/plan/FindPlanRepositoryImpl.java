package co.jp.wever.graphql.infrastructure.repository.plan;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.WithOptions;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.repository.plan.FindPlanRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.edge.label.PlanToPlanElementEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.PlanToTagEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToPlanEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.property.PlanToPlanElementProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.converter.entity.plan.PlanDetailEntityConverter;
import co.jp.wever.graphql.infrastructure.converter.entity.plan.PlanListItemEntityConverter;
import co.jp.wever.graphql.infrastructure.converter.entity.plan.PlansEntityConverter;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanBaseEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.aggregation.PlanDetailEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.aggregation.PlanListItemEntity;

@Component
public class FindPlanRepositoryImpl implements FindPlanRepository {
    private final NeptuneClient neptuneClient;

    public FindPlanRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    @Override
    public PlanDetailEntity findOne(String planId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        //        Map<String, Object> result = g.V(planId)
        //                                      .project("base",
        //                                               "tags",
        //                                               "author",
        //                                               "action",
        //                                               "status",
        //                                               "elements",
        //                                               "like",
        //                                               "learned",
        //                                               "learning")
        //                                      .by(__.valueMap().with(WithOptions.tokens))
        //                                      .by(__.out(PlanToTagEdge.RELATED.getString())
        //                                            .hasLabel(VertexLabel.TAG.getString())
        //                                            .valueMap()
        //                                            .with(WithOptions.tokens)
        //                                            .fold())
        //                                      .by(__.in(UserToPlanEdge.DRAFTED.getString(),
        //                                                UserToPlanEdge.PUBLISHED.getString())
        //                                            .hasLabel(VertexLabel.USER.getString())
        //                                            .valueMap()
        //                                            .with(WithOptions.tokens))
        //                                      .by(__.inE(UserToPlanEdge.LIKED.getString(),
        //                                                 UserToPlanEdge.LEARNED.getString(),
        //                                                 UserToPlanEdge.LEARNING.getString()).label().fold())
        //                                      .by(__.inE(UserToPlanEdge.DRAFTED.getString(),
        //                                                 UserToPlanEdge.PUBLISHED.getString()).label())
        //                                      .by(__.out(PlanToPlanElementEdge.INCLUDE.getString())
        //                                            .hasLabel(VertexLabel.ARTICLE.getString(), VertexLabel.PLAN.getString())
        //                                            .project("element", "number")
        //                                            .by(__.label()
        //                                                  .values(ArticleVertexProperty.IMAGE_URL.getString(),
        //                                                          ArticleVertexProperty.TITLE.getString())
        //                                                  .with(WithOptions.tokens)
        //                                                  .fold())
        //                                            .by(__.outE()
        //                                                  .hasLabel(PlanToPlanElementEdge.INCLUDE.getString())
        //                                                  .values(PlanToPlanElementProperty.NUMBER.getString())
        //                                                  .fold()))
        //                                      .by(__.in(UserToPlanEdge.LIKED.getString()).count())
        //                                      .by(__.in(UserToPlanEdge.LEARNED.getString()).count())
        //                                      .by(__.in(UserToPlanEdge.LEARNING.getString()).count())
        //                                      .next();


        Map<String, Object> result = g.V(planId)
                                      .project("base",
                                               "tags",
                                               "author",
                                               "action",
                                               "status",
                                               "elements",
                                               "like",
                                               "learned",
                                               "learning")
                                      .by(__.valueMap().with(WithOptions.tokens))
                                      .by(__.out(PlanToTagEdge.RELATED.getString())
                                            .hasLabel(VertexLabel.TAG.getString())
                                            .valueMap()
                                            .with(WithOptions.tokens)
                                            .fold())
                                      .by(__.in(UserToPlanEdge.DRAFTED.getString(),
                                                UserToPlanEdge.PUBLISHED.getString())
                                            .hasLabel(VertexLabel.USER.getString())
                                            .valueMap()
                                            .with(WithOptions.tokens))
                                      .by(__.inE(UserToPlanEdge.LIKED.getString(),
                                                 UserToPlanEdge.LEARNED.getString(),
                                                 UserToPlanEdge.LEARNING.getString()).label().fold())
                                      .by(__.inE(UserToPlanEdge.DRAFTED.getString(),
                                                 UserToPlanEdge.PUBLISHED.getString()).label())
                                      .by(__.out(PlanToPlanElementEdge.INCLUDE.getString())
                                            .hasLabel(VertexLabel.ARTICLE.getString(), VertexLabel.PLAN.getString())
                                            .project("element", "number")
                                            .by(__.valueMap().with(WithOptions.tokens))
                                            .by(__.inE(PlanToPlanElementEdge.INCLUDE.getString())
                                                  .values(PlanToPlanElementProperty.NUMBER.getString()))
                                            .fold())
                                      .by(__.in(UserToPlanEdge.LIKED.getString()).count())
                                      .by(__.in(UserToPlanEdge.LEARNED.getString()).count())
                                      .by(__.in(UserToPlanEdge.LEARNING.getString()).count())
                                      .next();
        return PlanDetailEntityConverter.toPlanDetailEntity(result);
    }


    @Override
    public List<PlanListItemEntity> findAll(String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        //TODO: このクエリがどうなるか確認
        // OKなら下の個別のクエリは不要
        List<Map<String, Object>> allResult = g.V(userId)
                                               .out(UserToPlanEdge.DRAFTED.getString(),
                                                    UserToPlanEdge.PUBLISHED.getString())
                                               .hasLabel(VertexLabel.PLAN.getString())
                                               .project("base", "tags", "status", "liked", "learned", "learning")
                                               .by(__.valueMap().with(WithOptions.tokens))
                                               .by(__.out(PlanToTagEdge.RELATED.getString())
                                                     .hasLabel(VertexLabel.TAG.getString())
                                                     .valueMap()
                                                     .with(WithOptions.tokens)
                                                     .fold())
                                               .by(__.inE(UserToPlanEdge.DRAFTED.getString(),
                                                          UserToPlanEdge.PUBLISHED.getString()).label())
                                               .by(__.in(UserToPlanEdge.LIKED.getString()).count())
                                               .by(__.in(UserToPlanEdge.LEARNED.getString()).count())
                                               .by(__.in(UserToPlanEdge.LEARNING.getString()).count())
                                               .toList();
        return allResult.stream()
                        .map(r -> PlanListItemEntityConverter.toPlanListItemEntity(r))
                        .collect(Collectors.toList());
    }

    @Override
    public List<PlanEntity> findAllPublished(String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<Object, Object>> result = g.V(userId)
                                            .out(UserToPlanEdge.PUBLISHED.getString())
                                            .has("type", VertexLabel.PLAN.getString())
                                            .valueMap()
                                            .with(WithOptions.tokens)
                                            .toList();

        return PlansEntityConverter.toPlans(result);
    }

    @Override
    public List<PlanEntity> findAllDrafted(String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<Object, Object>> result = g.V(userId)
                                            .out(UserToPlanEdge.DRAFTED.getString())
                                            .has("type", VertexLabel.PLAN.getString())
                                            .valueMap()
                                            .with(WithOptions.tokens)
                                            .toList();

        return PlansEntityConverter.toPlans(result);
    }

    @Override
    public List<PlanEntity> findAllLiked(String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<Object, Object>> result = g.V(userId)
                                            .out(UserToPlanEdge.LIKED.getString())
                                            .has("type", VertexLabel.PLAN.getString())
                                            .valueMap()
                                            .with(WithOptions.tokens)
                                            .toList();

        return PlansEntityConverter.toPlans(result);
    }

    @Override
    public List<PlanEntity> findAllLearning(String userId) {

        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<Object, Object>> result = g.V(userId)
                                            .out(UserToPlanEdge.LEARNING.getString())
                                            .has("type", VertexLabel.PLAN.getString())
                                            .valueMap()
                                            .with(WithOptions.tokens)
                                            .toList();

        return PlansEntityConverter.toPlans(result);
    }

    @Override
    public List<PlanEntity> findAllLearned(String userId) {

        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<Object, Object>> result = g.V(userId)
                                            .out(UserToPlanEdge.LEARNED.getString())
                                            .has("type", VertexLabel.PLAN.getString())
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
                                            .out(UserToPlanEdge.PUBLISHED.getString())
                                            .has("type", VertexLabel.PLAN.getString())
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
                                            .out(UserToPlanEdge.PUBLISHED.getString())
                                            .has("type", VertexLabel.PLAN.getString())
                                            .valueMap()
                                            .with(WithOptions.tokens)
                                            .toList();

        return PlansEntityConverter.toPlans(result);
    }

    @Override
    public PlanBaseEntity findBase(String planId) {
        //        GraphTraversalSource g = neptuneClient.newTraversal();
        //
        //        Map<Object, Object> planResult = g.V(planId).valueMap().with(WithOptions.tokens).next();
        //
        //        Map<Object, Object> tagResult =
        //            g.V(planId).out(PlanToTagEdge.RELATED.getString()).valueMap().with(WithOptions.tokens).next();
        //
        //        Map<Object, Object> userResult = g.V(planId).in().valueMap().with(WithOptions.tokens).next();
        //
        //        Map<Object, Object> statusResult =
        //            g.V(planId).out(PlanToTagEdge.RELATED.getString()).valueMap().with(WithOptions.tokens).next();

        return PlanBaseEntity.builder().build();
    }

    @Override
    public List<String> findPublishedPlanElementIds(List<String> planElementIds) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        //
        //        List<String> results = g.V(planElementIds)
        //                                .hasLabel(VertexLabel.PLAN.getString(), VertexLabel.ARTICLE.getString())
        //                                .outE()
        //                                .label()
        //                                .toList();

        //        System.out.println(results);
        // IDだけ取得する
        //TODO: 型がどうなってるか要検証
        //        return PlanEntityConverter.toPlan(result);

        return null;
    }

    @Override
    public String findAuthorId(String planId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        return (String) g.V(planId)
                         .in(UserToPlanEdge.PUBLISHED.getString(), UserToPlanEdge.DRAFTED.getString())
                         .hasLabel(VertexLabel.USER.getString())
                         .id()
                         .next();
    }
}
