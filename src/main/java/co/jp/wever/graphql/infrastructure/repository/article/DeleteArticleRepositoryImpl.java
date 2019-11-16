package co.jp.wever.graphql.infrastructure.repository.article;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.T;
import org.springframework.stereotype.Component;

import co.jp.wever.graphql.domain.repository.article.DeleteArticleRepository;
import co.jp.wever.graphql.infrastructure.connector.AlgoliaClient;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToArticleEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.property.UserToArticleProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.util.EdgeIdCreator;

import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.outV;

@Component
public class DeleteArticleRepositoryImpl implements DeleteArticleRepository {
    private final NeptuneClient neptuneClient;
    private final AlgoliaClient algoliaClient;

    public DeleteArticleRepositoryImpl(
        NeptuneClient neptuneClient, AlgoliaClient algoliaClient) {
        this.neptuneClient = neptuneClient;
        this.algoliaClient = algoliaClient;
    }

    public void deleteOne(String articleId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        g.V(articleId)
         .inE(UserToArticleEdge.PUBLISHED.getString(), UserToArticleEdge.DRAFTED.getString())
         .where(outV().hasLabel(VertexLabel.USER.getString()).hasId(userId))
         .drop()
         .iterate();

        g.V(userId)
         .addE(UserToArticleEdge.DELETED.getString())
         .property(T.id, EdgeIdCreator.createId(userId, articleId, UserToArticleEdge.DELETED.getString()))
         .to(g.V(articleId))
         .property(UserToArticleProperty.CREATED_TIME.getString(), now)
         .property(UserToArticleProperty.UPDATED_TIME.getString(), now)
         .next();

        algoliaClient.getArticleIndex().deleteObjectAsync(articleId);
    }
}
