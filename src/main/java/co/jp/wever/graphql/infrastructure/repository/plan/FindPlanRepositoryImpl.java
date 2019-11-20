package co.jp.wever.graphql.infrastructure.repository.plan;

import org.apache.tinkerpop.gremlin.process.traversal.Order;
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
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.PlanVertexProperty;
import co.jp.wever.graphql.infrastructure.converter.entity.plan.PlanItemEntityConverter;
import co.jp.wever.graphql.infrastructure.converter.entity.plan.PlanDetailEntityConverter;
import co.jp.wever.graphql.infrastructure.converter.entity.plan.PlanElementDetailEntityConverter;
import co.jp.wever.graphql.infrastructure.converter.entity.plan.PlanLearningItemEntityConverter;
import co.jp.wever.graphql.infrastructure.converter.entity.plan.PlanListItemEntityConverter;
import co.jp.wever.graphql.infrastructure.converter.entity.plan.PlansEntityConverter;
import co.jp.wever.graphql.infrastructure.datamodel.plan.LearningPlanItemEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanElementDetailEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.aggregation.PlanItemEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.aggregation.PlanDetailEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.aggregation.PlanListItemEntity;

import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.constant;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.inE;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.outV;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.values;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.coalesce;

@Component
public class FindPlanRepositoryImpl implements FindPlanRepository {
    private final NeptuneClient neptuneClient;

    public FindPlanRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    @Override
    public PlanListItemEntity findOne(String planId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        Map<String, Object> result = g.V(planId)
                                      .hasLabel(VertexLabel.PLAN.getString())
                                      .project("base",
                                               "tags",
                                               "status",
                                               "userLiked",
                                               "userLearned",
                                               "userLearning",
                                               "liked",
                                               "learned",
                                               "learning")
                                      .by(__.valueMap().with(WithOptions.tokens))
                                      .by(__.out(PlanToTagEdge.RELATED.getString())
                                            .hasLabel(VertexLabel.TAG.getString())
                                            .valueMap()
                                            .with(WithOptions.tokens)
                                            .fold())
                                      .by(__.inE(UserToPlanEdge.DRAFTED.getString(),
                                                 UserToPlanEdge.PUBLISHED.getString()).label())
                                      .by(coalesce(__.inE(UserToPlanEdge.LIKED.getString())
                                                     .where(outV().hasId(userId).hasLabel(VertexLabel.USER.getString()))
                                                     .limit(1)
                                                     .constant(true), constant(false)))
                                      .by(coalesce(__.inE(UserToPlanEdge.LEARNED.getString())
                                                     .where(outV().hasId(userId).hasLabel(VertexLabel.USER.getString()))
                                                     .limit(1)
                                                     .constant(true), constant(false)))
                                      .by(coalesce(__.inE(UserToPlanEdge.LEARNING.getString())
                                                     .where(outV().hasId(userId).hasLabel(VertexLabel.USER.getString()))
                                                     .limit(1)
                                                     .constant(true), constant(false)))
                                      .by(__.in(UserToPlanEdge.LIKED.getString()).count())
                                      .by(__.in(UserToPlanEdge.LEARNED.getString()).count())
                                      .by(__.in(UserToPlanEdge.LEARNING.getString()).count())
                                      .next();
        return PlanListItemEntityConverter.toPlanListItemEntity(result);
    }

    @Override
    public PlanDetailEntity findDetail(String planId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
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
                                            .project("element", "edge")
                                            .by(__.valueMap().with(WithOptions.tokens))
                                            .by(__.inE(PlanToPlanElementEdge.INCLUDE.getString())
                                                  .where(outV().hasId(planId))
                                                  .valueMap())
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

        List<Map<String, Object>> allResult = g.V(userId)
                                               .out(UserToPlanEdge.DRAFTED.getString(),
                                                    UserToPlanEdge.PUBLISHED.getString())
                                               .hasLabel(VertexLabel.PLAN.getString())
                                               .project("base",
                                                        "tags",
                                                        "status",
                                                        "userLiked",
                                                        "userLearned",
                                                        "userLearning",
                                                        "liked",
                                                        "learned",
                                                        "learning")
                                               .by(__.valueMap().with(WithOptions.tokens))
                                               .by(__.out(PlanToTagEdge.RELATED.getString())
                                                     .hasLabel(VertexLabel.TAG.getString())
                                                     .valueMap()
                                                     .with(WithOptions.tokens)
                                                     .fold())
                                               .by(__.inE(UserToPlanEdge.DRAFTED.getString(),
                                                          UserToPlanEdge.PUBLISHED.getString()).label())
                                               .by(coalesce(__.inE(UserToPlanEdge.LIKED.getString())
                                                              .where(outV().hasId(userId)
                                                                           .hasLabel(VertexLabel.USER.getString()))
                                                              .limit(1)
                                                              .constant(true), constant(false)))
                                               .by(coalesce(__.inE(UserToPlanEdge.LEARNED.getString())
                                                              .where(outV().hasId(userId)
                                                                           .hasLabel(VertexLabel.USER.getString()))
                                                              .limit(1)
                                                              .constant(true), constant(false)))
                                               .by(coalesce(__.inE(UserToPlanEdge.LEARNING.getString())
                                                              .where(outV().hasId(userId)
                                                                           .hasLabel(VertexLabel.USER.getString()))
                                                              .limit(1)
                                                              .constant(true), constant(false)))
                                               .by(__.in(UserToPlanEdge.LIKED.getString()).count())
                                               .by(__.in(UserToPlanEdge.LEARNED.getString()).count())
                                               .by(__.in(UserToPlanEdge.LEARNING.getString()).count())
                                               .toList();

        return allResult.stream()
                        .map(r -> PlanListItemEntityConverter.toPlanListItemEntity(r))
                        .collect(Collectors.toList());
    }

    @Override
    public List<PlanListItemEntity> findAllPublished(String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<String, Object>> allResult = g.V(userId)
                                               .out(UserToPlanEdge.PUBLISHED.getString())
                                               .hasLabel(VertexLabel.PLAN.getString())
                                               .project("base",
                                                        "tags",
                                                        "status",
                                                        "userLiked",
                                                        "userLearned",
                                                        "userLearning",
                                                        "liked",
                                                        "learned",
                                                        "learning")
                                               .by(__.valueMap().with(WithOptions.tokens))
                                               .by(__.out(PlanToTagEdge.RELATED.getString())
                                                     .hasLabel(VertexLabel.TAG.getString())
                                                     .valueMap()
                                                     .with(WithOptions.tokens)
                                                     .fold())
                                               .by(__.inE(UserToPlanEdge.PUBLISHED.getString()).label())
                                               .by(coalesce(__.inE(UserToPlanEdge.LIKED.getString())
                                                              .where(outV().hasId(userId)
                                                                           .hasLabel(VertexLabel.USER.getString()))
                                                              .limit(1)
                                                              .constant(true), constant(false)))
                                               .by(coalesce(__.inE(UserToPlanEdge.LEARNED.getString())
                                                              .where(outV().hasId(userId)
                                                                           .hasLabel(VertexLabel.USER.getString()))
                                                              .limit(1)
                                                              .constant(true), constant(false)))
                                               .by(coalesce(__.inE(UserToPlanEdge.LEARNING.getString())
                                                              .where(outV().hasId(userId)
                                                                           .hasLabel(VertexLabel.USER.getString()))
                                                              .limit(1)
                                                              .constant(true), constant(false)))
                                               .by(__.in(UserToPlanEdge.LIKED.getString()).count())
                                               .by(__.in(UserToPlanEdge.LEARNED.getString()).count())
                                               .by(__.in(UserToPlanEdge.LEARNING.getString()).count())
                                               .toList();


        return allResult.stream()
                        .map(r -> PlanListItemEntityConverter.toPlanListItemEntity(r))
                        .collect(Collectors.toList());
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
    public List<PlanItemEntity> findAllLiked(String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        List<Map<String, Object>> results = g.V(userId)
                                             .out(UserToPlanEdge.LIKED.getString())
                                             .hasLabel(VertexLabel.PLAN.getString())
                                             .project("base",
                                                      "tags",
                                                      "author",
                                                      "status",
                                                      "like",
                                                      "learned",
                                                      "learning",
                                                      "elementCount")
                                             .by(__.valueMap().with(WithOptions.tokens))
                                             .by(__.out(PlanToTagEdge.RELATED.getString())
                                                   .hasLabel(VertexLabel.TAG.getString())
                                                   .valueMap()
                                                   .with(WithOptions.tokens)
                                                   .fold())
                                             .by(__.in(UserToPlanEdge.DRAFTED.getString(),
                                                       UserToPlanEdge.DELETED.getString(),
                                                       UserToPlanEdge.PUBLISHED.getString())
                                                   .hasLabel(VertexLabel.USER.getString())
                                                   .valueMap()
                                                   .with(WithOptions.tokens))
                                             .by(__.inE(UserToPlanEdge.DRAFTED.getString(),
                                                        UserToPlanEdge.DELETED.getString(),
                                                        UserToPlanEdge.PUBLISHED.getString()).label())
                                             .by(__.in(UserToPlanEdge.LIKED.getString()).count())
                                             .by(__.in(UserToPlanEdge.LEARNED.getString()).count())
                                             .by(__.in(UserToPlanEdge.LEARNING.getString()).count())
                                             .by(__.outE(PlanToPlanElementEdge.INCLUDE.getString()).count())
                                             .toList();
        return results.stream().map(r -> PlanItemEntityConverter.toPlanItemEntity(r)).collect(Collectors.toList());
    }

    @Override
    public List<LearningPlanItemEntity> findAllLearning(String userId) {

        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<String, Object>> results = g.V(userId)
                                             .out(UserToPlanEdge.LEARNING.getString())
                                             .hasLabel(VertexLabel.PLAN.getString())
                                             .project("base",
                                                      "allElements",
                                                      "learnedElements",
                                                      "liked",
                                                      "learned",
                                                      "learning")
                                             .by(__.valueMap().with(WithOptions.tokens))
                                             .by(__.out(PlanToPlanElementEdge.INCLUDE.getString()).label().fold())
                                             .by(__.out(PlanToPlanElementEdge.INCLUDE.getString())
                                                   .hasLabel(VertexLabel.ARTICLE.getString(),
                                                             VertexLabel.PLAN.getString())
                                                   .inE(UserToPlanEdge.LEARNED.getString())
                                                   .where(outV().hasId(userId).hasLabel(VertexLabel.USER.getString()))
                                                   .label()
                                                   .fold())
                                             .by(__.in(UserToPlanEdge.LIKED.getString()).count())
                                             .by(__.in(UserToPlanEdge.LEARNED.getString()).count())
                                             .by(__.in(UserToPlanEdge.LEARNING.getString()).count())
                                             .toList();

        return results.stream()
                      .map(r -> PlanLearningItemEntityConverter.toPlanLearningItemEntity(r))
                      .collect(Collectors.toList());
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
    public List<String> findAllPlanElementIds(String planId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        return g.V(planId)
                .out(PlanToPlanElementEdge.INCLUDE.getString())
                .hasLabel(VertexLabel.PLAN.getString(), VertexLabel.ARTICLE.getString())
                .id()
                .toList()
                .stream()
                .map(o -> (String) o)
                .collect(Collectors.toList());

    }

    @Override
    public List<PlanElementDetailEntity> findAllPlanElementDetails(String planId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<String, Object>> results = g.V(planId)
                                             .out(PlanToPlanElementEdge.INCLUDE.getString())
                                             .hasLabel(VertexLabel.PLAN.getString(), VertexLabel.ARTICLE.getString())
                                             .project("base", "edge", "userLiked", "userLearned", "like", "learned")
                                             .by(__.valueMap().with(WithOptions.tokens))
                                             .by(__.inE(PlanToPlanElementEdge.INCLUDE.getString())
                                                   .where(outV().hasId(planId))
                                                   .valueMap())
                                             .by(coalesce(__.inE(UserToPlanEdge.LIKED.getString())
                                                            .where(outV().hasId(userId)
                                                                         .hasLabel(VertexLabel.USER.getString()))
                                                            .limit(1)
                                                            .constant(true), constant(false)))
                                             .by(coalesce(__.inE(UserToPlanEdge.LEARNED.getString())
                                                            .where(outV().hasId(userId)
                                                                         .hasLabel(VertexLabel.USER.getString()))
                                                            .limit(1)
                                                            .constant(true), constant(false)))
                                             .by(__.in(UserToPlanEdge.LIKED.getString()).count())
                                             .by(__.in(UserToPlanEdge.LEARNED.getString()).count())
                                             .toList();

        return results.stream()
                      .map(r -> PlanElementDetailEntityConverter.toPlanElementDetailEntity(r))
                      .collect(Collectors.toList());
    }

    @Override
    public List<PlanElementDetailEntity> findAllPlanElementDetailsForGuest(String planId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<String, Object>> results = g.V(planId)
                                             .out(PlanToPlanElementEdge.INCLUDE.getString())
                                             .hasLabel(VertexLabel.PLAN.getString(), VertexLabel.ARTICLE.getString())
                                             .project("base", "edge", "like", "learned")
                                             .by(__.valueMap().with(WithOptions.tokens))
                                             .by(__.inE(PlanToPlanElementEdge.INCLUDE.getString())
                                                   .where(outV().hasId(planId))
                                                   .valueMap())
                                             .by(__.in(UserToPlanEdge.LIKED.getString()).count())
                                             .by(__.in(UserToPlanEdge.LEARNED.getString()).count())
                                             .toList();

        return results.stream()
                      .map(r -> PlanElementDetailEntityConverter.toPlanElementDetailEntityForGuest(r))
                      .collect(Collectors.toList());
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

    public List<PlanItemEntity> findFamous() {
        GraphTraversalSource g = neptuneClient.newTraversal();

        List<Map<String, Object>> results = g.V()
                                             .hasLabel(VertexLabel.PLAN.getString())
                                             .filter(inE().hasLabel(UserToPlanEdge.PUBLISHED.getString()))
                                             .order()
                                             .by(values(PlanVertexProperty.LEARNING.getString(),
                                                        PlanVertexProperty.LIKED.getString(),
                                                        PlanVertexProperty.LEARNING.getString()).sum(), Order.desc)
                                             .limit(10)
                                             .project("base",
                                                      "tags",
                                                      "author",
                                                      "status",
                                                      "like",
                                                      "learned",
                                                      "learning",
                                                      "elementCount")
                                             .by(__.valueMap().with(WithOptions.tokens))
                                             .by(__.out(PlanToTagEdge.RELATED.getString())
                                                   .hasLabel(VertexLabel.TAG.getString())
                                                   .valueMap()
                                                   .with(WithOptions.tokens)
                                                   .fold())
                                             .by(__.in(UserToPlanEdge.DRAFTED.getString(),
                                                       UserToPlanEdge.DELETED.getString(),
                                                       UserToPlanEdge.PUBLISHED.getString())
                                                   .hasLabel(VertexLabel.USER.getString())
                                                   .valueMap()
                                                   .with(WithOptions.tokens))
                                             .by(__.inE(UserToPlanEdge.DRAFTED.getString(),
                                                        UserToPlanEdge.DELETED.getString(),
                                                        UserToPlanEdge.PUBLISHED.getString()).label())
                                             .by(__.in(UserToPlanEdge.LIKED.getString()).count())
                                             .by(__.in(UserToPlanEdge.LEARNED.getString()).count())
                                             .by(__.in(UserToPlanEdge.LEARNING.getString()).count())
                                             .by(__.outE(PlanToPlanElementEdge.INCLUDE.getString()).count())
                                             .toList();
        return results.stream().map(r -> PlanItemEntityConverter.toPlanItemEntity(r)).collect(Collectors.toList());
    }
}
