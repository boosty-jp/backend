package co.jp.wever.graphql.infrastructure.repository.article;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.springframework.stereotype.Component;

import java.util.List;

import co.jp.wever.graphql.domain.repository.article.UpdateArticleRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.edge.label.ArticleToTagEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToArticleEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.property.UserToArticleProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.ArticleVertexProperty;

import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.outV;
import static org.apache.tinkerpop.gremlin.structure.VertexProperty.Cardinality.single;

@Component
public class UpdateArticleRepositoryImpl implements UpdateArticleRepository {

    private final NeptuneClient neptuneClient;

    public UpdateArticleRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    //
    //    @Override
    //    public void updateOne(ArticleBaseEntity targetArticle, List<String> tagIds) {
    //        GraphTraversalSource g = neptuneClient.newTraversal();
    //
    //        long now = System.currentTimeMillis() / 1000L;
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

        long now = System.currentTimeMillis() / 1000L;

        g.V(articleId)
         .hasLabel(VertexLabel.ARTICLE.getString())
         .property(single, ArticleVertexProperty.TITLE.getString(), title)
         .property(single, ArticleVertexProperty.UPDATED_TIME.getString(), now)
         .next();

        // TODO: Algolia更新
    }

    @Override
    public void updateImageUrl(String articleId, String imageUrl) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        long now = System.currentTimeMillis() / 1000L;

        g.V(articleId)
         .hasLabel(VertexLabel.ARTICLE.getString())
         .property(single, ArticleVertexProperty.IMAGE_URL.getString(), imageUrl)
         .property(single, ArticleVertexProperty.UPDATED_TIME.getString(), now)
         .next();

        // TODO: Algolia更新
    }

    @Override
    public void updateTags(String articleId, List<String> tags) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        long now = System.currentTimeMillis() / 1000L;

        g.V(articleId)
         .hasLabel(VertexLabel.ARTICLE.getString())
         .property(single, ArticleVertexProperty.UPDATED_TIME.getString(), now)
         .next();

        // タグの張替え
        // TODO:タグの変更がないときは更新しないようにしたい
        g.V(articleId)
         .hasLabel(VertexLabel.ARTICLE.getString())
         .outE(ArticleToTagEdge.RELATED.getString())
         .drop()
         .iterate();

        g.V(tags)
         .hasLabel(VertexLabel.TAG.getString())
         .addE(ArticleToTagEdge.RELATED.getString())
         .from(g.V(articleId))
         .next();

        // TODO: Algolia更新
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

        long now = System.currentTimeMillis() / 1000L;

        g.V(userId)
         .hasLabel(VertexLabel.USER.getString())
         .addE(UserToArticleEdge.PUBLISHED.getString())
         .to(g.V(articleId).hasLabel(VertexLabel.USER.getString()))
         .property(UserToArticleProperty.PUBLISHED_TIME.getString(), now)
         .next();

        // TODO: Algoliaに追加
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

        long now = System.currentTimeMillis() / 1000L;

        g.V(userId)
         .hasLabel(VertexLabel.USER.getString())
         .addE(UserToArticleEdge.DRAFTED.getString())
         .to(g.V(articleId).hasLabel(VertexLabel.USER.getString()))
         .property(UserToArticleProperty.DRAFTED_TIME.getString(), now)
         .next();

        // TODO: Algoliaに追加
    }

    @Override
    public void likeOne(String articleId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        // 二重に登録されるのを防止する
        // TODO: いいやり方検討する
        g.V(articleId)
         .hasLabel(VertexLabel.USER.getString())
         .inE(UserToArticleEdge.LIKED.getString())
         .where(outV().hasId(userId).hasLabel(VertexLabel.USER.getString()))
         .drop()
         .iterate();

        long now = System.currentTimeMillis() / 1000L;
        g.V(articleId)
         .hasLabel(VertexLabel.ARTICLE.getString())
         .addE(UserToArticleEdge.LIKED.getString())
         .from(g.V(userId).hasLabel(VertexLabel.USER.getString()))
         .property(UserToArticleProperty.LIKED_TIME.getString(), now)
         .next();
    }

    @Override
    public void finishOne(String articleId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        // 二重に登録されるのを防止する
        // TODO: いいやり方検討する
        g.V(articleId).inE(UserToArticleEdge.LEARNED.getString()).where(outV().hasId(userId)).drop().iterate();

        long now = System.currentTimeMillis() / 1000L;
        g.V(articleId)
         .hasLabel(VertexLabel.ARTICLE.getString())
         .addE(UserToArticleEdge.LEARNED.getString())
         .from(g.V(userId).hasLabel(VertexLabel.USER.getString()))
         .property(UserToArticleProperty.LEARNED_TIME.getString(), now)
         .next();
    }
}
