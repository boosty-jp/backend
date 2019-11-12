package co.jp.wever.graphql.infrastructure.repository.article;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.T;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.domainmodel.article.DraftArticle;
import co.jp.wever.graphql.domain.domainmodel.article.PublishArticle;
import co.jp.wever.graphql.domain.repository.article.UpdateArticleRepository;
import co.jp.wever.graphql.infrastructure.connector.AlgoliaClient;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.edge.label.ArticleToSectionEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.ArticleToTagEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.PlanToTagEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToArticleEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToPlanEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.property.ArticleToSectionProperty;
import co.jp.wever.graphql.infrastructure.constant.edge.property.ArticleToTagProperty;
import co.jp.wever.graphql.infrastructure.constant.edge.property.UserToArticleProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.ArticleVertexProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.TagVertexProperty;
import co.jp.wever.graphql.infrastructure.converter.entity.article.ArticleSearchEntityConverter;
import co.jp.wever.graphql.infrastructure.converter.entity.section.SectionSearchEntityConverter;
import co.jp.wever.graphql.infrastructure.datamodel.algolia.ArticleSearchEntity;
import co.jp.wever.graphql.infrastructure.util.EdgeIdCreator;

import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.inE;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.inV;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.outV;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.unfold;
import static org.apache.tinkerpop.gremlin.structure.VertexProperty.Cardinality.single;

@Component
public class UpdateArticleRepositoryImpl implements UpdateArticleRepository {

    private final NeptuneClient neptuneClient;
    private final AlgoliaClient algoliaClient;

    public UpdateArticleRepositoryImpl(
        NeptuneClient neptuneClient, AlgoliaClient algoliaClient) {
        this.neptuneClient = neptuneClient;
        this.algoliaClient = algoliaClient;
    }

    @Override
    public void updateTitle(String articleId, String title) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        g.V(articleId)
         .hasLabel(VertexLabel.ARTICLE.getString())
         .property(single, ArticleVertexProperty.TITLE.getString(), title)
         .property(single, ArticleVertexProperty.UPDATED_TIME.getString(), now)
         .next();
    }

    @Override
    public void updateImageUrl(String articleId, String imageUrl) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        g.V(articleId)
         .hasLabel(VertexLabel.ARTICLE.getString())
         .property(single, ArticleVertexProperty.IMAGE_URL.getString(), imageUrl)
         .property(single, ArticleVertexProperty.UPDATED_TIME.getString(), now)
         .next();
    }

    @Override
    public void updateTags(String articleId, List<String> tagIds) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        g.V(articleId)
         .hasLabel(VertexLabel.ARTICLE.getString())
         .property(single, ArticleVertexProperty.UPDATED_TIME.getString(), now)
         .next();

        g.V(articleId)
         .hasLabel(VertexLabel.ARTICLE.getString())
         .outE(ArticleToTagEdge.RELATED.getString())
         .where(inV().hasLabel(VertexLabel.TAG.getString()))
         .drop()
         .iterate();

        if (!tagIds.isEmpty()) {
            g.V(tagIds)
             .hasLabel(VertexLabel.TAG.getString())
             .addE(ArticleToTagEdge.RELATED.getString())
             .property(ArticleToTagProperty.CREATED_TIME.getString(), now)
             .property(ArticleToTagProperty.UPDATED_TIME.getString(), now)
             .from(g.V(articleId))
             .next();
        }
    }

    @Override
    public void publishOne(PublishArticle publishArticle, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        g.V(publishArticle.getId())
         .hasLabel(VertexLabel.ARTICLE.getString())
         .outE(ArticleToTagEdge.RELATED.getString())
         .where(inV().hasLabel(VertexLabel.TAG.getString()))
         .drop()
         .iterate();

        if (!publishArticle.getTagIds().isEmpty()) {
            g.V(publishArticle.getTagIds())
             .hasLabel(VertexLabel.TAG.getString())
             .addE(ArticleToTagEdge.RELATED.getString())
             .property(ArticleToTagProperty.CREATED_TIME.getString(), now)
             .property(ArticleToTagProperty.UPDATED_TIME.getString(), now)
             .from(g.V(publishArticle.getId()))
             .iterate();
        }



        g.V(publishArticle.getId())
         .hasLabel(VertexLabel.ARTICLE.getString())
         .property(single, ArticleVertexProperty.TITLE.getString(), publishArticle.getTitle())
         .property(single, ArticleVertexProperty.IMAGE_URL.getString(), publishArticle.getImageUrl())
         .property(single, ArticleVertexProperty.UPDATED_TIME.getString(), now)
         .next();

        g.V(publishArticle.getId())
         .hasLabel(VertexLabel.ARTICLE.getString())
         .outE(ArticleToSectionEdge.INCLUDE.getString())
         .where(inV().hasLabel(VertexLabel.SECTION.getString()))
         .drop()
         .iterate();

        publishArticle.getUpdateSection().stream().forEach(e -> {
            g.V(publishArticle.getId())
             .hasLabel(VertexLabel.ARTICLE.getString())
             .addE(ArticleToSectionEdge.INCLUDE.getString())
             .property(ArticleToSectionProperty.NUMBER.getString(), e.getNumber())
             .property(ArticleToSectionProperty.CREATED_TIME.getString(), now)
             .property(ArticleToSectionProperty.UPDATED_TIME.getString(), now)
             .to(g.V(e.getId()))
             .iterate();
        });

        g.E(EdgeIdCreator.createId(userId, publishArticle.getId(), UserToArticleEdge.PUBLISHED.getString()))
         .fold()
         .coalesce(unfold(),
                   g.V(userId)
                    .hasLabel(VertexLabel.USER.getString())
                    .addE(UserToArticleEdge.PUBLISHED.getString())
                    .property(T.id,
                              EdgeIdCreator.createId(userId,
                                                     publishArticle.getId(),
                                                     UserToArticleEdge.PUBLISHED.getString()))
                    .to(g.V(publishArticle.getId()).hasLabel(VertexLabel.ARTICLE.getString()))
                    .property(UserToArticleProperty.CREATED_TIME.getString(), now)
                    .property(UserToArticleProperty.UPDATED_TIME.getString(), now))
         .next();

        // このクエリの位置はここより上にしないこと。この記事のタグへのひも付きをカウントできなくなる。
        publishArticle.getTagIds().forEach(t -> {
            long related = g.V(t)
                            .inE(PlanToTagEdge.RELATED.getString())
                            .where(outV().hasLabel(VertexLabel.PLAN.getString(), VertexLabel.ARTICLE.getString())
                                         .filter(inE().hasLabel(UserToPlanEdge.PUBLISHED.getString())))
                            .count()
                            .next();

            g.V(t).property(single, TagVertexProperty.RELATED.getString(), related).next();
        });

        g.V(publishArticle.getId())
         .hasLabel(VertexLabel.ARTICLE.getString())
         .inE(UserToArticleEdge.DRAFTED.getString(), UserToArticleEdge.DELETED.getString())
         .where(outV().hasId(userId).hasLabel(VertexLabel.USER.getString()))
         .drop()
         .iterate();

        algoliaClient.getArticleIndex()
                     .saveObjectAsync(ArticleSearchEntityConverter.toArticleSearchEntity(publishArticle, now, userId));

        algoliaClient.getSectionIndex()
                     .saveObjectsAsync(publishArticle.getUpdateSection()
                                                     .stream()
                                                     .map(r -> SectionSearchEntityConverter.toSectionSearchEntity(r,
                                                                                                                  now,
                                                                                                                  userId))
                                                     .collect(Collectors.toList()));
    }

    @Override
    public void draftOne(DraftArticle draftArticle, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        g.V(draftArticle.getId())
         .hasLabel(VertexLabel.ARTICLE.getString())
         .outE(ArticleToTagEdge.RELATED.getString())
         .where(inV().hasLabel(VertexLabel.TAG.getString()))
         .drop()
         .iterate();

        if (!draftArticle.getTagIds().isEmpty()) {
            g.V(draftArticle.getTagIds())
             .hasLabel(VertexLabel.TAG.getString())
             .addE(ArticleToTagEdge.RELATED.getString())
             .property(ArticleToTagProperty.CREATED_TIME.getString(), now)
             .property(ArticleToTagProperty.UPDATED_TIME.getString(), now)
             .from(g.V(draftArticle.getId()))
             .iterate();
        }

        g.V(draftArticle.getId())
         .hasLabel(VertexLabel.ARTICLE.getString())
         .property(single, ArticleVertexProperty.TITLE.getString(), draftArticle.getTitle())
         .property(single, ArticleVertexProperty.IMAGE_URL.getString(), draftArticle.getImageUrl())
         .property(single, ArticleVertexProperty.UPDATED_TIME.getString(), now)
         .next();

        g.V(draftArticle.getId())
         .hasLabel(VertexLabel.ARTICLE.getString())
         .outE(ArticleToSectionEdge.INCLUDE.getString())
         .where(inV().hasLabel(VertexLabel.SECTION.getString()))
         .drop()
         .iterate();

        draftArticle.getUpdateSection().stream().forEach(e -> {
            g.V(draftArticle.getId())
             .hasLabel(VertexLabel.ARTICLE.getString())
             .addE(ArticleToSectionEdge.INCLUDE.getString())
             .property(ArticleToSectionProperty.NUMBER.getString(), e.getNumber())
             .property(ArticleToSectionProperty.UPDATED_TIME.getString(), now)
             .to(g.V(e.getId()))
             .iterate();
        });

        g.E(EdgeIdCreator.createId(userId, draftArticle.getId(), UserToArticleEdge.DRAFTED.getString()))
         .fold()
         .coalesce(unfold(),
                   g.V(userId)
                    .hasLabel(VertexLabel.USER.getString())
                    .addE(UserToArticleEdge.DRAFTED.getString())
                    .property(T.id,
                              EdgeIdCreator.createId(userId,
                                                     draftArticle.getId(),
                                                     UserToArticleEdge.DRAFTED.getString()))
                    .to(g.V(draftArticle.getId()).hasLabel(VertexLabel.ARTICLE.getString()))
                    .property(UserToArticleProperty.CREATED_TIME.getString(), now)
                    .property(UserToArticleProperty.UPDATED_TIME.getString(), now))
         .next();

        g.V(draftArticle.getId())
         .hasLabel(VertexLabel.ARTICLE.getString())
         .inE(UserToArticleEdge.PUBLISHED.getString(), UserToArticleEdge.DELETED.getString())
         .where(outV().hasId(userId).hasLabel(VertexLabel.USER.getString()))
         .drop()
         .iterate();

        algoliaClient.getArticleIndex().deleteObjectAsync(draftArticle.getId());
        List<String> draftSectionIds =
            draftArticle.getUpdateSection().stream().map(s -> s.getId()).collect(Collectors.toList());
        if (!draftSectionIds.isEmpty()) {
            algoliaClient.getSectionIndex().deleteObjectsAsync(draftSectionIds);
        }
    }

    @Override
    public void likeOne(String articleId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        g.E(EdgeIdCreator.createId(userId, articleId, UserToArticleEdge.LIKED.getString()))
         .fold()
         .coalesce(unfold(),
                   g.V(articleId)
                    .hasLabel(VertexLabel.ARTICLE.getString())
                    .addE(UserToArticleEdge.LIKED.getString())
                    .property(T.id, EdgeIdCreator.createId(userId, articleId, UserToArticleEdge.LIKED.getString()))
                    .property(UserToArticleProperty.CREATED_TIME.getString(), now)
                    .property(UserToArticleProperty.UPDATED_TIME.getString(), now)
                    .from(g.V(userId).hasLabel(VertexLabel.USER.getString())))
         .next();

        long like = g.V(articleId).inE(UserToArticleEdge.LIKED.getString()).count().next();
        g.V(articleId).property(single, ArticleVertexProperty.LIKED.getString(), like).next();
        algoliaClient.getArticleIndex()
                     .partialUpdateObjectAsync(ArticleSearchEntity.builder().objectID(articleId).like(like).build());
    }

    @Override
    public void deleteLikeOne(String articleId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        g.V(articleId)
         .inE(UserToArticleEdge.LIKED.getString())
         .hasId(EdgeIdCreator.createId(userId, articleId, UserToArticleEdge.LIKED.getString()))
         .drop()
         .iterate();

        long like = g.V(articleId).inE(UserToArticleEdge.LIKED.getString()).count().next();
        g.V(articleId).property(single, ArticleVertexProperty.LIKED.getString(), like).next();
        algoliaClient.getArticleIndex()
                     .partialUpdateObjectAsync(ArticleSearchEntity.builder().objectID(articleId).like(like).build());
    }

    @Override
    public void finishOne(String articleId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        g.E(UserToArticleEdge.LEARNED.getString())
         .hasId(EdgeIdCreator.createId(userId, articleId, UserToArticleEdge.LEARNED.getString()))
         .fold()
         .coalesce(unfold(),
                   g.V(articleId)
                    .hasLabel(VertexLabel.ARTICLE.getString())
                    .addE(UserToArticleEdge.LEARNED.getString())
                    .property(T.id, EdgeIdCreator.createId(userId, articleId, UserToArticleEdge.LEARNED.getString()))
                    .property(UserToArticleProperty.CREATED_TIME.getString(), now)
                    .property(UserToArticleProperty.UPDATED_TIME.getString(), now)
                    .from(g.V(userId).hasLabel(VertexLabel.USER.getString())))
         .next();

        long learned = g.V(articleId).inE(UserToArticleEdge.LEARNED.getString()).count().next();
        g.V(articleId).property(single, ArticleVertexProperty.LEARNED.getString(), learned).next();

        algoliaClient.getArticleIndex()
                     .partialUpdateObjectAsync(ArticleSearchEntity.builder()
                                                                  .objectID(articleId)
                                                                  .learned(learned)
                                                                  .build());
    }

    @Override
    public void deleteFinishOne(String articleId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        g.V(articleId)
         .inE(UserToArticleEdge.LEARNED.getString())
         .hasId(EdgeIdCreator.createId(userId, articleId, UserToArticleEdge.LEARNED.getString()))
         .drop()
         .iterate();

        long learned = g.V(articleId).inE(UserToArticleEdge.LEARNED.getString()).count().next();
        g.V(articleId).property(single, ArticleVertexProperty.LEARNED.getString(), learned).next();
        algoliaClient.getArticleIndex()
                     .partialUpdateObjectAsync(ArticleSearchEntity.builder()
                                                                  .objectID(articleId)
                                                                  .learned(learned)
                                                                  .build());
    }
}
