package co.jp.wever.graphql.infrastructure.repository.plan;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.T;
import org.springframework.stereotype.Component;

import java.util.List;

import co.jp.wever.graphql.domain.domainmodel.plan.DraftPlan;
import co.jp.wever.graphql.domain.domainmodel.plan.PublishPlan;
import co.jp.wever.graphql.domain.repository.plan.UpdatePlanRepository;
import co.jp.wever.graphql.infrastructure.connector.AlgoliaClient;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.PlanVertexProperty;
import co.jp.wever.graphql.infrastructure.converter.entity.plan.PlanSearchEntityConverter;
import co.jp.wever.graphql.infrastructure.datamodel.algolia.PlanSearchEntity;
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
        long now = System.currentTimeMillis();

        g.V(planId)
         .hasLabel(VertexLabel.COURSE.getString())
         .property(single, PlanVertexProperty.TITLE.getString(), title)
         .property(single, PlanVertexProperty.UPDATED_TIME.getString(), now)
         .next();
    }

    @Override
    public void updateImageUrl(String planId, String imageUrl) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        g.V(planId)
         .hasLabel(VertexLabel.COURSE.getString())
         .property(single, PlanVertexProperty.IMAGE_URL.getString(), imageUrl)
         .property(single, PlanVertexProperty.UPDATED_TIME.getString(), now)
         .next();
    }

    @Override
    public void updateDescription(String planId, String description) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        g.V(planId)
         .hasLabel(VertexLabel.COURSE.getString())
         .property(single, PlanVertexProperty.DESCRIPTION.getString(), description)
         .property(single, PlanVertexProperty.UPDATED_TIME.getString(), now)
         .next();
    }

    @Override
    public void updateTags(String planId, List<String> tagIds) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        g.V(planId)
         .hasLabel(VertexLabel.COURSE.getString())
         .property(single, PlanVertexProperty.UPDATED_TIME.getString(), now)
         .next();

        g.V(planId)
         .hasLabel(VertexLabel.COURSE.getString())
         .outE(PlanToTagEdge.RELATED.getString())
         .where(inV().hasLabel(VertexLabel.TAG.getString()))
         .drop()
         .iterate();

        if (!tagIds.isEmpty()) {
            g.V(tagIds)
             .hasLabel(VertexLabel.TAG.getString())
             .addE(PlanToTagEdge.RELATED.getString())
             .property(PlanToTagProperty.CREATED_TIME.getString(), now)
             .property(PlanToTagProperty.UPDATED_TIME.getString(), now)
             .from(g.V(planId))
             .iterate();
        }
    }

    @Override
    public void publishOne(PublishPlan publishPlan, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        g.V(publishPlan.getId())
         .hasLabel(VertexLabel.COURSE.getString())
         .property(single, PlanVertexProperty.TITLE.getString(), publishPlan.getTitle())
         .property(single, PlanVertexProperty.DESCRIPTION.getString(), publishPlan.getDescription())
         .property(single, PlanVertexProperty.IMAGE_URL.getString(), publishPlan.getImageUrl())
         .property(single, PlanVertexProperty.UPDATED_TIME.getString(), now)
         .next();

        g.V(publishPlan.getId())
         .hasLabel(VertexLabel.COURSE.getString())
         .outE(PlanToPlanElementEdge.INCLUDE.getString())
         .drop()
         .iterate();

        publishPlan.getElements().stream().forEach(e -> {
            g.V(publishPlan.getId())
             .hasLabel(VertexLabel.COURSE.getString())
             .addE(PlanToPlanElementEdge.INCLUDE.getString())
             .property(PlanToPlanElementProperty.NUMBER.getString(), e.getNumber())
             .property(PlanToPlanElementProperty.CREATED_TIME.getString(), now)
             .property(PlanToPlanElementProperty.UPDATED_TIME.getString(), now)
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
             .property(PlanToTagProperty.CREATED_TIME.getString(), now)
             .property(PlanToTagProperty.UPDATED_TIME.getString(), now)
             .from(g.V(publishPlan.getId()))
             .iterate();
        }

        g.E(EdgeIdCreator.createId(userId, publishPlan.getId(), UserToPlanEdge.PUBLISHED.getString()))
         .fold()
         .coalesce(unfold(),
                   g.V(userId)
                    .addE(UserToPlanEdge.PUBLISHED.getString())
                    .property(T.id,
                              EdgeIdCreator.createId(userId, publishPlan.getId(), UserToPlanEdge.PUBLISHED.getString()))
                    .to(g.V(publishPlan.getId()))
                    .property(UserToPlanProperty.CREATED_TIME.getString(), now))
         .next();

        g.V(publishPlan.getId())
         .inE(UserToPlanEdge.DRAFTED.getString(), UserToPlanEdge.DELETED.getString())
         .where(outV().hasId(userId).hasLabel(VertexLabel.USER.getString()))
         .drop()
         .iterate();

        algoliaClient.getPlanIndex()
                     .saveObjectAsync(PlanSearchEntityConverter.toPlanSearchEntity(publishPlan, userId, now));
    }

    @Override
    public void draftOne(DraftPlan draftPlan, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        g.V(draftPlan.getId())
         .hasLabel(VertexLabel.COURSE.getString())
         .property(single, PlanVertexProperty.TITLE.getString(), draftPlan.getTitle())
         .property(single, PlanVertexProperty.DESCRIPTION.getString(), draftPlan.getDescription())
         .property(single, PlanVertexProperty.IMAGE_URL.getString(), draftPlan.getImageUrl())
         .property(single, PlanVertexProperty.UPDATED_TIME.getString(), now)
         .next();

        g.V(draftPlan.getId())
         .hasLabel(VertexLabel.COURSE.getString())
         .outE(PlanToPlanElementEdge.INCLUDE.getString())
         .drop()
         .iterate();

        draftPlan.getElements().stream().forEach(e -> {
            g.V(draftPlan.getId())
             .hasLabel(VertexLabel.COURSE.getString())
             .addE(PlanToPlanElementEdge.INCLUDE.getString())
             .property(PlanToPlanElementProperty.NUMBER.getString(), e.getNumber())
             .property(PlanToPlanElementProperty.CREATED_TIME.getString(), now)
             .property(PlanToPlanElementProperty.UPDATED_TIME.getString(), now)
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
             .property(PlanToTagProperty.CREATED_TIME.getString(), now)
             .property(PlanToTagProperty.UPDATED_TIME.getString(), now)
             .from(g.V(draftPlan.getId()))
             .iterate();
        }

        g.E(EdgeIdCreator.createId(userId, draftPlan.getId(), UserToPlanEdge.DRAFTED.getString()))
         .fold()
         .coalesce(unfold(),
                   g.V(userId)
                    .addE(UserToPlanEdge.DRAFTED.getString())
                    .property(T.id,
                              EdgeIdCreator.createId(userId, draftPlan.getId(), UserToPlanEdge.DRAFTED.getString()))
                    .to(g.V(draftPlan.getId()))
                    .property(UserToPlanProperty.CREATED_TIME.getString(), now)
                    .property(UserToPlanProperty.UPDATED_TIME.getString(), now))
         .next();

        g.V(draftPlan.getId())
         .inE(UserToPlanEdge.PUBLISHED.getString(), UserToPlanEdge.DELETED.getString())
         .where(outV().hasId(userId).hasLabel(VertexLabel.USER.getString()))
         .drop()
         .iterate();

        algoliaClient.getPlanIndex().deleteObjectAsync(draftPlan.getId());
    }

    @Override
    public void likeOne(String planId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        g.E(EdgeIdCreator.createId(userId, planId, UserToPlanEdge.LIKED.getString()))
         .fold()
         .coalesce(unfold(),
                   g.V(userId)
                    .addE(UserToPlanEdge.LIKED.getString())
                    .property(T.id, EdgeIdCreator.createId(userId, planId, UserToPlanEdge.LIKED.getString()))
                    .property(UserToPlanProperty.CREATED_TIME.getString(), now)
                    .property(UserToPlanProperty.UPDATED_TIME.getString(), now)
                    .to(g.V(planId)))
         .iterate();

        long liked = g.V(planId).inE(UserToPlanEdge.LIKED.getString()).count().next();
        g.V(planId).property(single, PlanVertexProperty.LIKED.getString(), liked).next();
        algoliaClient.getPlanIndex()
                     .partialUpdateObjectAsync(PlanSearchEntity.builder().objectID(planId).like(liked).build());
    }

    @Override
    public void deleteLikeOne(String planId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        g.E(EdgeIdCreator.createId(userId, planId, UserToPlanEdge.LIKED.getString())).drop().iterate();

        long liked = g.V(planId).inE(UserToPlanEdge.LIKED.getString()).count().next();
        g.V(planId).property(single, PlanVertexProperty.LIKED.getString(), liked).next();
        algoliaClient.getPlanIndex()
                     .partialUpdateObjectAsync(PlanSearchEntity.builder().objectID(planId).like(liked).build());
    }

    @Override
    public void startOne(String planId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        g.V(planId)
         .hasLabel(VertexLabel.COURSE.getString())
         .inE(UserToPlanEdge.LEARNED.getString())
         .where(outV().hasId(userId).hasLabel(VertexLabel.USER.getString()))
         .drop()
         .iterate();

        g.E(EdgeIdCreator.createId(userId, planId, UserToPlanEdge.LEARNING.getString()))
         .fold()
         .coalesce(unfold(),
                   g.V(userId)
                    .addE(UserToPlanEdge.LEARNING.getString())
                    .property(T.id, EdgeIdCreator.createId(userId, planId, UserToPlanEdge.LEARNING.getString()))
                    .property(UserToPlanProperty.CREATED_TIME.getString(), now)
                    .property(UserToPlanProperty.UPDATED_TIME.getString(), now)
                    .to(g.V(planId)))
         .iterate();

        long learning = g.V(planId).inE(UserToPlanEdge.LEARNING.getString()).count().next();
        g.V(planId).property(single, PlanVertexProperty.LEARNING.getString(), learning).next();
        algoliaClient.getPlanIndex()
                     .partialUpdateObjectAsync(PlanSearchEntity.builder().objectID(planId).learning(learning).build());
    }

    @Override
    public void finishOne(String planId, String userId, List<String> elementIds) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();
        g.V(planId)
         .hasLabel(VertexLabel.COURSE.getString())
         .inE(UserToPlanEdge.LEARNING.getString())
         .where(outV().hasId(userId).hasLabel(VertexLabel.USER.getString()))
         .drop()
         .iterate();

        elementIds.forEach(e -> {
            // TODO: 対象のVertexのLEARNEDを更新するようにしたい
            // 実際に学習した数とズレが生じてしまうが、クエリが重いため許容している
            g.E(EdgeIdCreator.createId(userId, e, UserToPlanEdge.LEARNED.getString()))
             .fold()
             .coalesce(unfold(),
                       g.V(userId)
                        .addE(UserToPlanEdge.LEARNED.getString())
                        .property(T.id, EdgeIdCreator.createId(userId, e, UserToPlanEdge.LEARNED.getString()))
                        .property(UserToPlanProperty.CREATED_TIME.getString(), now)
                        .property(UserToPlanProperty.UPDATED_TIME.getString(), now)
                        .to(g.V(e)))
             .iterate();
        });

        g.E(EdgeIdCreator.createId(userId, planId, UserToPlanEdge.LEARNED.getString()))
         .fold()
         .coalesce(unfold(),
                   g.V(userId)
                    .addE(UserToPlanEdge.LEARNED.getString())
                    .property(T.id, EdgeIdCreator.createId(userId, planId, UserToPlanEdge.LEARNED.getString()))
                    .property(UserToPlanProperty.CREATED_TIME.getString(), now)
                    .property(UserToPlanProperty.UPDATED_TIME.getString(), now)
                    .to(g.V(planId)))
         .iterate();

        long learning = g.V(planId).inE(UserToPlanEdge.LEARNING.getString()).count().next();
        long learned = g.V(planId).inE(UserToPlanEdge.LEARNED.getString()).count().next();
        g.V(planId)
         .property(single, PlanVertexProperty.LEARNING.getString(), learning)
         .property(single, PlanVertexProperty.LEARNED.getString(), learned)
         .next();
        algoliaClient.getPlanIndex()
                     .partialUpdateObjectAsync(PlanSearchEntity.builder()
                                                               .objectID(planId)
                                                               .learning(learning)
                                                               .learned(learned)
                                                               .build());
    }


    @Override
    public void stopOne(String planId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        g.V(planId)
         .inE(UserToPlanEdge.LEARNED.getString())
         .where(outV().hasId(userId).hasLabel(VertexLabel.USER.getString()))
         .drop()
         .iterate();

        long learning = g.V(planId).inE(UserToPlanEdge.LEARNING.getString()).count().next();
        long learned = g.V(planId).inE(UserToPlanEdge.LEARNED.getString()).count().next();
        g.V(planId)
         .property(single, PlanVertexProperty.LEARNING.getString(), learning)
         .property(single, PlanVertexProperty.LEARNED.getString(), learned)
         .next();
        algoliaClient.getPlanIndex()
                     .partialUpdateObjectAsync(PlanSearchEntity.builder()
                                                               .objectID(planId)
                                                               .learning(learning)
                                                               .learned(learned)
                                                               .build());
    }
}
