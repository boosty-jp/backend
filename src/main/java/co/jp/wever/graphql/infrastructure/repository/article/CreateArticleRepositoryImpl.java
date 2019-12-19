package co.jp.wever.graphql.infrastructure.repository.article;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.T;
import org.springframework.stereotype.Component;


import co.jp.wever.graphql.domain.repository.article.CreateArticleRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.ArticleVertexProperty;
import co.jp.wever.graphql.infrastructure.util.EdgeIdCreator;

@Component
public class CreateArticleRepositoryImpl implements CreateArticleRepository {
    private final NeptuneClient neptuneClient;

    public CreateArticleRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    @Override
    public String initOne(String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        long now = System.currentTimeMillis();

        String articleId = g.addV(VertexLabel.ARTICLE.getString())
                            .property(ArticleVertexProperty.TITLE.getString(), "")
                            .property(ArticleVertexProperty.IMAGE_URL.getString(), "")
                            .property(ArticleVertexProperty.LIKED.getString(), 0)
                            .property(ArticleVertexProperty.LEARNED.getString(), 0)
                            .property(ArticleVertexProperty.CREATED_TIME.getString(), now)
                            .property(ArticleVertexProperty.UPDATED_TIME.getString(), now)
                            .next()
                            .id()
                            .toString();

        g.V(articleId)
         .addE(UserToArticleEdge.DRAFTED.getString())
         .property(T.id, EdgeIdCreator.createId(userId, articleId, UserToArticleEdge.DRAFTED.getString()))
         .from(g.V(userId))
         .property(UserToArticleProperty.CREATED_TIME.getString(), now)
         .property(UserToArticleProperty.UPDATED_TIME.getString(), now)
         .next();
        return articleId;
    }
}
