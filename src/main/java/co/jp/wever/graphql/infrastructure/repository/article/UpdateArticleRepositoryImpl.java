package co.jp.wever.graphql.infrastructure.repository.article;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.WithOptions;
import org.apache.tinkerpop.gremlin.structure.T;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.repository.article.UpdateArticleRepository;
import co.jp.wever.graphql.infrastructure.connector.AlgoliaClient;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.edge.label.ArticleToSectionEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.ArticleToTagEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToArticleEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToSectionEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.property.UserToArticleProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.ArticleVertexProperty;
import co.jp.wever.graphql.infrastructure.converter.entity.article.ArticleSearchEntityConverter;
import co.jp.wever.graphql.infrastructure.converter.entity.section.SectionSearchEntityConverter;
import co.jp.wever.graphql.infrastructure.datamodel.algolia.ArticleSearchEntity;
import co.jp.wever.graphql.infrastructure.util.EdgeIdCreator;

import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.constant;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.inV;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.outV;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.coalesce;
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

    //
    //    @Override
    //    public void updateOne(ArticleBaseEntity targetArticle, List<String> tagIds) {
    //        GraphTraversalSource g = neptuneClient.newTraversal();
    //
    //        long now = System.currentTimeMillis();
    //
    //        g.V(targetArticle.getId())
    //         .property(single, ArticleVertexProperty.TITLE.getString(), targetArticle.getTitle())
    //         .property(single, ArticleVertexProperty.IMAGE_URL.getString(), targetArticle.getImageUrl())
    //         .property(single, ArticleVertexProperty.UPDATED_TIME.getString(), now)
    //         .next();
    //
    //        // タグの張替え
    //        // TODO:タグの変更がないときは更新しないようにしたい
    //        g.V(targetArticle.getId()).outE(ArticleToTagEdge.RELATED.getString()).drop().iterate();
    //        g.V(tagIds).addE(PlanToTagEdge.RELATED.getString()).from(g.V(targetArticle.getId())).next();
    //
    //        // TODO: Algolia更新
    //    }

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
    public void updateTags(String articleId, List<String> tags) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        long now = System.currentTimeMillis();

        g.V(articleId)
         .hasLabel(VertexLabel.ARTICLE.getString())
         .property(single, ArticleVertexProperty.UPDATED_TIME.getString(), now)
         .next();

        // タグの張替え
        // TODO:タグの変更がないときは更新しないようにしたい
        g.V(articleId)
         .hasLabel(VertexLabel.ARTICLE.getString())
         .outE(ArticleToTagEdge.RELATED.getString())
         .where(inV().hasLabel(VertexLabel.TAG.getString()))
         .drop()
         .iterate();

        if(!tags.isEmpty()){
            g.V(tags)
             .hasLabel(VertexLabel.TAG.getString())
             .addE(ArticleToTagEdge.RELATED.getString())
             .from(g.V(articleId))
             .next();
        }
    }

    @Override
    public void publishOne(String articleId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        g.V(articleId)
         .hasLabel(VertexLabel.ARTICLE.getString())
         .inE(UserToArticleEdge.DRAFTED.getString(), UserToArticleEdge.DELETED.getString())
         .where(outV().hasId(userId).hasLabel(VertexLabel.USER.getString()))
         .drop()
         .iterate();

        long now = System.currentTimeMillis();

        g.E(EdgeIdCreator.userPublishArticle(userId, articleId))
         .fold()
         .coalesce(unfold(),
                   g.V(userId)
                    .addE(UserToArticleEdge.PUBLISHED.getString())
                    .property(T.id, EdgeIdCreator.userPublishArticle(userId, articleId))
                    .to(g.V(articleId).hasLabel(VertexLabel.ARTICLE.getString()))
                    .property(UserToArticleProperty.PUBLISHED_TIME.getString(), now))
         .iterate();

        // Algoliaに追加
        Map<String, Object> result = g.V(articleId)
                                      .project("base", "tags", "liked", "learned")
                                      .by(__.valueMap().with(WithOptions.tokens))
                                      .by(__.out(ArticleToTagEdge.RELATED.getString())
                                            .hasLabel(VertexLabel.TAG.getString())
                                            .valueMap()
                                            .with(WithOptions.tokens)
                                            .fold())
                                      .by(__.in(UserToArticleEdge.LIKED.getString()).count())
                                      .by(__.in(UserToArticleEdge.LEARNED.getString()).count())
                                      .by(coalesce(__.inE(UserToSectionEdge.LIKED.getString())
                                                     .where(outV().hasId(userId)
                                                                  .hasLabel(VertexLabel.USER.getString()))
                                                     .limit(1)
                                                     .constant(true), constant(false)))
                                      .next();

        algoliaClient.getArticleIndex()
                     .saveObjectAsync(ArticleSearchEntityConverter.toArticleSearchEntity(result,
                                                                                         userId,
                                                                                         articleId,
                                                                                         now));

        List<Map<String, Object>> results = g.V(articleId)
                                             .out(ArticleToSectionEdge.INCLUDE.getString())
                                             .hasLabel(VertexLabel.SECTION.getString())
                                             .project("base", "liked")
                                             .by(__.valueMap().with(WithOptions.tokens))
                                             .by(__.in(UserToSectionEdge.LIKED.getString()).count())
                                             .toList();

        algoliaClient.getSectionIndex()
                     .saveObjectsAsync(results.stream()
                                              .map(r -> SectionSearchEntityConverter.toSectionSearchEntity(r,
                                                                                                           userId,
                                                                                                           now))
                                              .collect(Collectors.toList()));
    }

    @Override
    public void draftOne(String articleId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        g.V(articleId)
         .hasLabel(VertexLabel.ARTICLE.getString())
         .inE(UserToArticleEdge.PUBLISHED.getString(), UserToArticleEdge.DELETED.getString())
         .where(outV().hasId(userId).hasLabel(VertexLabel.USER.getString()))
         .drop()
         .iterate();

        long now = System.currentTimeMillis();

        g.E(EdgeIdCreator.userDraftArticle(userId, articleId))
         .fold()
         .coalesce(unfold(),
                   g.V(userId)
                    .hasLabel(VertexLabel.USER.getString())
                    .addE(UserToArticleEdge.DRAFTED.getString())
                    .property(T.id, EdgeIdCreator.userDraftArticle(userId, articleId))
                    .to(g.V(articleId).hasLabel(VertexLabel.ARTICLE.getString()))
                    .property(UserToArticleProperty.DRAFTED_TIME.getString(), now))
         .next();

        // Algoliaから削除
        // TODO: バッチにして更新頻度下げる
        algoliaClient.getArticleIndex().deleteObjectAsync(articleId);

        List<Object> draftSectionIds = g.V(articleId)
                                        .out(ArticleToSectionEdge.INCLUDE.getString())
                                        .hasLabel(VertexLabel.SECTION.getString())
                                        .id()
                                        .toList();

        if(!draftSectionIds.isEmpty()){
            algoliaClient.getSectionIndex()
                         .deleteObjectsAsync(draftSectionIds.stream().map(r -> (String) r).collect(Collectors.toList()));
        }
    }

    @Override
    public void likeOne(String articleId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        long now = System.currentTimeMillis();

        g.E(UserToArticleEdge.LIKED.getString())
         .hasId(EdgeIdCreator.userLikeArticle(userId, articleId))
         .fold()
         .coalesce(unfold(),
                   g.V(articleId)
                    .hasLabel(VertexLabel.ARTICLE.getString())
                    .addE(UserToArticleEdge.LIKED.getString())
                    .property(T.id, EdgeIdCreator.userLikeArticle(userId, articleId))
                    .property(UserToArticleProperty.LIKED_TIME.getString(), now)
                    .from(g.V(userId).hasLabel(VertexLabel.USER.getString())))
         .next();

        // Algolia更新
        // TODO: バッチにして更新頻度下げる
        long like = g.V(articleId)
                     .in(UserToArticleEdge.LIKED.getString())
                     .hasLabel(VertexLabel.USER.getString())
                     .count()
                     .next();
        algoliaClient.getArticleIndex()
                     .partialUpdateObjectAsync(ArticleSearchEntity.builder().objectID(articleId).like(like).build());
    }

    @Override
    public void deleteLikeOne(String articleId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        g.V(articleId)
         .inE(UserToArticleEdge.LIKED.getString())
         .hasId(EdgeIdCreator.userLikeArticle(userId, articleId))
         .drop()
         .iterate();

        // Algolia更新
        // TODO: バッチにして更新頻度下げる
        long like = g.V(articleId)
                     .in(UserToArticleEdge.LIKED.getString())
                     .hasLabel(VertexLabel.USER.getString())
                     .count()
                     .next();
        algoliaClient.getArticleIndex()
                     .partialUpdateObjectAsync(ArticleSearchEntity.builder().objectID(articleId).like(like).build());
    }

    @Override
    public void finishOne(String articleId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        long now = System.currentTimeMillis();


        g.E(UserToArticleEdge.LEARNED.getString())
         .hasId(EdgeIdCreator.userLearnArticle(userId, articleId))
         .fold()
         .coalesce(unfold(),
                   g.V(articleId)
                    .hasLabel(VertexLabel.ARTICLE.getString())
                    .addE(UserToArticleEdge.LEARNED.getString())
                    .property(T.id, EdgeIdCreator.userLearnArticle(userId, articleId))
                    .property(UserToArticleProperty.LEARNED_TIME.getString(), now)
                    .from(g.V(userId).hasLabel(VertexLabel.USER.getString())))
         .next();

        // Algolia更新
        // TODO: バッチにして更新頻度下げる
        long learned = g.V(articleId)
                        .in(UserToArticleEdge.LEARNED.getString())
                        .hasLabel(VertexLabel.USER.getString())
                        .count()
                        .next();
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
         .hasId(EdgeIdCreator.userLearnArticle(userId, articleId))
         .drop()
         .iterate();

        // Algolia更新
        // TODO: バッチにして更新頻度下げる
        long learned = g.V(articleId)
                        .in(UserToArticleEdge.LEARNED.getString())
                        .hasLabel(VertexLabel.USER.getString())
                        .count()
                        .next();
        algoliaClient.getArticleIndex()
                     .partialUpdateObjectAsync(ArticleSearchEntity.builder()
                                                                  .objectID(articleId)
                                                                  .learned(learned)
                                                                  .build());
    }
}
