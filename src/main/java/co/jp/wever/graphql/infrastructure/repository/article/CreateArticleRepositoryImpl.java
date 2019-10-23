package co.jp.wever.graphql.infrastructure.repository.article;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.springframework.stereotype.Component;

import java.util.List;

import co.jp.wever.graphql.domain.repository.article.CreateArticleRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.edge.label.ArticleToTagEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToArticleEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.property.UserToArticleProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.ArticleVertexProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.PlanVertexProperty;
import co.jp.wever.graphql.infrastructure.datamodel.article.ArticleBaseEntity;

@Component
public class CreateArticleRepositoryImpl implements CreateArticleRepository {
    private final NeptuneClient neptuneClient;

    public CreateArticleRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }


    @Override
    public String createOne(ArticleBaseEntity articleBaseEntity, List<String> tagIds, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        long now = System.currentTimeMillis() / 1000L;
        String articleId = g.addV(VertexLabel.ARTICLE.getString())
                            .property(ArticleVertexProperty.TITLE.getString(), articleBaseEntity.getTitle())
                            .property(ArticleVertexProperty.DESCRIPTION.getString(), articleBaseEntity.getDescription())
                            .property(ArticleVertexProperty.IMAGE_URL.getString(), articleBaseEntity.getImageUrl())
                            .property(ArticleVertexProperty.CREATED_TIME.getString(), now)
                            .property(ArticleVertexProperty.UPDATED_TIME.getString(), now)
                            .next()
                            .id()
                            .toString();

        g.V(tagIds).addE(ArticleToTagEdge.RELATED.getString()).from(g.V(articleId)).next();

        g.V(articleId)
         .addE(UserToArticleEdge.DRAFTED.getString())
         .from(g.V(userId))
         .property(UserToArticleProperty.DRAFTED_TIME.getString(), now)
         .next();
        return articleId;
    }
}
