package co.jp.wever.graphql.infrastructure.repository.plan;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.T;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.domainmodel.plan.DraftPlan;
import co.jp.wever.graphql.domain.domainmodel.plan.PublishPlan;
import co.jp.wever.graphql.domain.repository.plan.UpdatePlanRepository;
import co.jp.wever.graphql.infrastructure.connector.AlgoliaClient;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import co.jp.wever.graphql.infrastructure.constant.edge.label.PlanToPlanElementEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.PlanToTagEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToPlanEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToSectionEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.property.PlanToPlanElementProperty;
import co.jp.wever.graphql.infrastructure.constant.edge.property.PlanToTagProperty;
import co.jp.wever.graphql.infrastructure.constant.edge.property.UserToPlanProperty;
import co.jp.wever.graphql.infrastructure.constant.edge.property.UserToSectionProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.PlanVertexProperty;
import co.jp.wever.graphql.infrastructure.converter.entity.plan.PlanSearchEntityConverter;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanElementEntity;
import co.jp.wever.graphql.infrastructure.util.EdgeIdCreator;

import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.inV;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.outV;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.unfold;
import static org.apache.tinkerpop.gremlin.structure.VertexProperty.Cardinality.single;

@Component
public class UpdatePlanRepositoryImpl implements UpdatePlanRepository {
    private final NeptuneClient neptuneClient;
    private final AlgoliaClient algoliaClient;

    public UpdatePlanRepositoryImpl(
        NeptuneClient neptuneClient, AlgoliaClient algoliaClient) {
        this.neptuneClient = neptuneClient;
        this.algoliaClient = algoliaClient;
    }

    @Override
    public void updateTitle(String planId, String title) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        g.V(planId)
         .hasLabel(VertexLabel.PLAN.getString())
         .property(single, PlanVertexProperty.TITLE.getString(), title)
         .next();
    }

    @Override
    public void updateImageUrl(String planId, String imageUrl) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        g.V(planId)
         .hasLabel(VertexLabel.PLAN.getString())
         .property(single, PlanVertexProperty.IMAGE_URL.getString(), imageUrl)
         .property(single, PlanVertexProperty.UPDATED_TIME.getString(), now)
         .next();
    }

    @Override
    public void updateDescription(String planId, String description) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        g.V(planId)
         .hasLabel(VertexLabel.PLAN.getString())
         .property(single, PlanVertexProperty.DESCRIPTION.getString(), description)
         .property(single, PlanVertexProperty.UPDATED_TIME.getString(), now)
         .next();
    }

    @Override
    public void updateTags(String planId, List<String> tagIds) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        g.V(planId)
         .hasLabel(VertexLabel.PLAN.getString())
         .property(single, PlanVertexProperty.UPDATED_TIME.getString(), now)
         .next();

        g.V(planId)
         .hasLabel(VertexLabel.PLAN.getString())
         .outE(PlanToTagEdge.RELATED.getString())
         .where(inV().hasLabel(VertexLabel.TAG.getString()))
         .drop()
         .iterate();

        if (!tagIds.isEmpty()) {
            g.V(tagIds)
             .hasLabel(VertexLabel.TAG.getString())
             .addE(PlanToTagEdge.RELATED.getString())
             .property(PlanToTagProperty.RELATED_TIME.getString(), now)
             .from(g.V(planId))
             .iterate();
        }
    }

    @Override
    public void updateElements(String planId, List<PlanElementEntity> planElementEntities) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        // エレメントの総張替え
        // TODO:変更がないエレメントは更新しないようにしたい
        g.V(planId)
         .out(PlanToTagEdge.RELATED.getString())
         .hasLabel(VertexLabel.ARTICLE.getString(), VertexLabel.PLAN.getString())
         .drop()
         .iterate();

        List<String> elementIds = planElementEntities.stream().map(e -> e.getId()).collect(Collectors.toList());
        g.V(elementIds).addE(PlanToTagEdge.RELATED.getString()).from(g.V(planId)).next();
    }

    @Override
    public void publishOne(PublishPlan publishPlan, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        g.V(publishPlan.getId())
         .hasLabel(VertexLabel.PLAN.getString())
         .property(single, PlanVertexProperty.TITLE.getString(), publishPlan.getTitle())
         .property(single, PlanVertexProperty.DESCRIPTION.getString(), publishPlan.getDescription())
         .property(single, PlanVertexProperty.IMAGE_URL.getString(), publishPlan.getImageUrl())
         .property(single, PlanVertexProperty.UPDATED_TIME.getString(), now)
         .next();

        g.V(publishPlan.getId())
         .hasLabel(VertexLabel.PLAN.getString())
         .outE(PlanToPlanElementEdge.INCLUDE.getString())
         .drop()
         .iterate();

        publishPlan.getElements().stream().forEach(e -> {
            g.V(publishPlan.getId())
             .hasLabel(VertexLabel.PLAN.getString())
             .addE(PlanToPlanElementEdge.INCLUDE.getString())
             .property(PlanToPlanElementProperty.NUMBER.getString(), e.getNumber())
             .property(PlanToPlanElementProperty.INCLUDE_TIME.getString(), now)
             .to(g.V(e.getId()))
             .iterate();
        });

        g.V(publishPlan.getId())
         .outE(PlanToTagEdge.RELATED.getString())
         .where(inV().hasLabel(VertexLabel.TAG.getString()))
         .drop()
         .iterate();

        if (!publishPlan.getTagIds().isEmpty()) {
            g.V(publishPlan.getTagIds())
             .hasLabel(VertexLabel.TAG.getString())
             .addE(PlanToTagEdge.RELATED.getString())
             .property(PlanToTagProperty.RELATED_TIME.getString(), now)
             .from(g.V(publishPlan.getId()))
             .iterate();
        }

        g.E(EdgeIdCreator.userPublishPlan(userId, publishPlan.getId()))
         .fold()
         .coalesce(unfold(),
                   g.V(userId)
                    .addE(UserToPlanEdge.PUBLISHED.getString())
                    .property(T.id, EdgeIdCreator.userPublishPlan(userId, publishPlan.getId()))
                    .to(g.V(publishPlan.getId()))
                    .property(UserToPlanProperty.PUBLISHED_TIME.getString(), now))
         .next();

        g.V(publishPlan.getId())
         .inE(UserToPlanEdge.DRAFTED.getString(), UserToPlanEdge.DELETED.getString())
         .where(outV().hasId(userId).hasLabel(VertexLabel.USER.getString()))
         .drop()
         .iterate();


        // TODO: Algoliaに追加
        algoliaClient.getPlanIndex()
                     .saveObjectAsync(PlanSearchEntityConverter.toPlanSearchEntity(publishPlan, userId, now));
    }

    @Override
    public void draftOne(DraftPlan draftPlan, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        g.V(draftPlan.getId())
         .hasLabel(VertexLabel.PLAN.getString())
         .property(single, PlanVertexProperty.TITLE.getString(), draftPlan.getTitle())
         .property(single, PlanVertexProperty.DESCRIPTION.getString(), draftPlan.getDescription())
         .property(single, PlanVertexProperty.IMAGE_URL.getString(), draftPlan.getImageUrl())
         .property(single, PlanVertexProperty.UPDATED_TIME.getString(), now)
         .next();

        g.V(draftPlan.getId())
         .hasLabel(VertexLabel.PLAN.getString())
         .outE(PlanToPlanElementEdge.INCLUDE.getString())
         .drop()
         .iterate();

        draftPlan.getElements().stream().forEach(e -> {
            g.V(draftPlan.getId())
             .hasLabel(VertexLabel.PLAN.getString())
             .addE(PlanToPlanElementEdge.INCLUDE.getString())
             .property(PlanToPlanElementProperty.NUMBER.getString(), e.getNumber())
             .property(PlanToPlanElementProperty.INCLUDE_TIME.getString(), now)
             .to(g.V(e.getId()))
             .iterate();
        });

        g.V(draftPlan.getId())
         .outE(PlanToTagEdge.RELATED.getString())
         .where(inV().hasLabel(VertexLabel.TAG.getString()))
         .drop()
         .iterate();

        if (!draftPlan.getTagIds().isEmpty()) {
            g.V(draftPlan.getTagIds())
             .hasLabel(VertexLabel.TAG.getString())
             .addE(PlanToTagEdge.RELATED.getString())
             .property(PlanToTagProperty.RELATED_TIME.getString(), now)
             .from(g.V(draftPlan.getId()))
             .iterate();
        }

        g.E(EdgeIdCreator.userDraftedPlan(userId, draftPlan.getId()))
         .fold()
         .coalesce(unfold(),
                   g.V(userId)
                    .addE(UserToPlanEdge.DRAFTED.getString())
                    .property(T.id, EdgeIdCreator.userDraftedPlan(userId, draftPlan.getId()))
                    .to(g.V(draftPlan.getId()))
                    .property(UserToPlanProperty.DRAFTED_TIME.getString(), now))
         .next();

        g.V(draftPlan.getId())
         .inE(UserToPlanEdge.PUBLISHED.getString(), UserToPlanEdge.DELETED.getString())
         .where(outV().hasId(userId).hasLabel(VertexLabel.USER.getString()))
         .drop()
         .iterate();

        algoliaClient.getPlanIndex().deleteObjectAsync(draftPlan.getId());
    }

    @Override
    public void startOne(String planId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();
        g.V(planId)
         .hasLabel(VertexLabel.PLAN.getString())
         .inE(UserToPlanEdge.LEARNED.getString())
         .where(outV().hasId(userId).hasLabel(VertexLabel.USER.getString()))
         .drop()
         .iterate();

        g.E(EdgeIdCreator.userLearningPlan(userId, planId))
         .fold()
         .coalesce(unfold(),
                   g.V(userId)
                    .addE(UserToPlanEdge.LEARNING.getString())
                    .property(T.id, EdgeIdCreator.userLearningPlan(userId, planId))
                    .property(UserToPlanProperty.LEARN_STARTED_TIME.getString(), now)
                    .to(g.V(planId)))
         .iterate();
    }

    @Override
    public void finishOne(String planId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();
        g.V(planId)
         .hasLabel(VertexLabel.PLAN.getString())
         .inE(UserToPlanEdge.LEARNING.getString())
         .where(outV().hasId(userId).hasLabel(VertexLabel.USER.getString()))
         .drop()
         .iterate();

        g.E(EdgeIdCreator.userLearnedPlan(userId, planId))
         .fold()
         .coalesce(unfold(),
                   g.V(userId)
                    .addE(UserToPlanEdge.LEARNED.getString())
                    .property(T.id, EdgeIdCreator.userLearnedPlan(userId, planId))
                    .property(UserToPlanProperty.LEARN_FINISHED_TIME.getString(), now)
                    .to(g.V(planId)))
         .iterate();
    }


    @Override
    public void stopOne(String planId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        g.V(planId)
         .inE(UserToPlanEdge.LEARNING.getString(), UserToPlanEdge.LEARNED.getString())
         .from(g.V(userId))
         .drop()
         .iterate();
    }
}
