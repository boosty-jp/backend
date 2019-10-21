package co.jp.wever.graphql.infrastructure.repository.article;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.springframework.stereotype.Component;

import java.util.List;

import co.jp.wever.graphql.domain.repository.article.CreateArticleRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToArticleEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.PlanToTagEdge;
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
        String articleId = g.addV(VertexLabel.ARTICLE.name())
                            .property(ArticleVertexProperty.TITLE.name(), articleBaseEntity.getTitle())
                            .property(ArticleVertexProperty.DESCRIPTION.name(), articleBaseEntity.getDescription())
                            .property(PlanVertexProperty.IMAGE_URL.name(), articleBaseEntity.getImageUrl())
                            .next()
                            .id()
                            .toString();

        g.V(tagIds).addE(PlanToTagEdge.RELATED.name()).from(g.V(articleId)).next();
        long now = System.currentTimeMillis() / 1000L;
        g.V(userId)
         .addE(UserToArticleEdge.DRAFTED.name())
         .to(g.V(articleId))
         .property(UserToArticleProperty.DRAFTED.name(), now)
         .next();
        return articleId;
    }
}
