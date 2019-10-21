package co.jp.wever.graphql.infrastructure.repository.article;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.repository.article.UpdateArticleRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.edge.label.PlanToTagEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToArticleEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToPlanEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.property.UserToPlanProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.ArticleVertexProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.PlanVertexProperty;
import co.jp.wever.graphql.infrastructure.datamodel.article.ArticleBaseEntity;
import co.jp.wever.graphql.infrastructure.datamodel.tag.TagEntity;

import static org.apache.tinkerpop.gremlin.structure.VertexProperty.Cardinality.single;

@Component
public class UpdateArticleRepositoryImpl implements UpdateArticleRepository {

    private final NeptuneClient neptuneClient;

    public UpdateArticleRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }


    @Override
    public void updateOne(ArticleBaseEntity targetArticle, List<String> tagIds) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        long now = System.currentTimeMillis() / 1000L;

        g.V(targetArticle.getId())
         .property(single, ArticleVertexProperty.TITLE.name(), targetArticle.getTitle())
         .property(single, ArticleVertexProperty.DESCRIPTION.name(), targetArticle.getDescription())
         .property(single, ArticleVertexProperty.IMAGE_URL.name(), targetArticle.getImageUrl())
         .property(single, ArticleVertexProperty.UPDATE_TIME.name(), now)
         .next();

        // タグの張替え
        // TODO:タグの変更がないときは更新しないようにしたい
        g.V(targetArticle.getId()).outE(PlanToTagEdge.RELATED.name()).hasLabel(VertexLabel.TAG.name()).drop();
        g.V(tagIds).addE(PlanToTagEdge.RELATED.name()).from(g.V(targetArticle.getId())).next();

        // TODO: Algolia更新
    }

    @Override
    public void publishOne(String articleId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        g.V(articleId).inE(UserToPlanEdge.DRAFT.name(), UserToPlanEdge.DELETE.name()).from(g.V(userId)).drop();

        long now = System.currentTimeMillis() / 1000L;

        g.V(userId)
         .addE(UserToArticleEdge.PUBLISHED.name())
         .to(g.V(articleId))
         .property(UserToPlanProperty.PUBLISHED.name(), now)
         .next();

        // TODO: Algoliaに追加
    }

    @Override
    public void draftOne(String articleId, String userId) {

    }

    @Override
    public void likeOne(String articleId, String userId) {
    }

    @Override
    public void finishOne(String articleId, String userId) {
    }
}
