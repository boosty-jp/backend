package co.jp.wever.graphql.infrastructure.repository.article;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.T;
import org.springframework.stereotype.Component;

import co.jp.wever.graphql.domain.repository.article.DeleteArticleRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToArticleEdge;
import co.jp.wever.graphql.infrastructure.util.EdgeIdCreator;

@Component
public class DeleteArticleRepositoryImpl implements DeleteArticleRepository {
    private final NeptuneClient neptuneClient;

    public DeleteArticleRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    public void deleteOne(String articleId, String userId) {

        GraphTraversalSource g = neptuneClient.newTraversal();

        // 著者のID指定したい
        g.V(articleId)
         .inE(UserToArticleEdge.PUBLISHED.getString(), UserToArticleEdge.DRAFTED.getString())
         .drop()
         .iterate();

        // 論理削除
        long now = System.currentTimeMillis();
        g.V(userId)
         .addE(UserToArticleEdge.DELETED.getString())
         .property(T.id, EdgeIdCreator.userDeleteArticle(userId, articleId))
         .to(g.V(articleId))
         .property(UserToArticleEdge.DELETED.getString(), now)
         .next();

        // TODO: Algoliaからデータを削除する
    }
}
