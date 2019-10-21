package co.jp.wever.graphql.infrastructure.repository.article;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.springframework.stereotype.Component;

import co.jp.wever.graphql.domain.repository.article.DeleteArticleRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToArticleEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToPlanEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.property.UserToPlanProperty;

@Component
public class DeleteArticleRepositoryImpl implements DeleteArticleRepository {
    private final NeptuneClient neptuneClient;

    public DeleteArticleRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    public void deleteOne(String articleId, String userId) {

        GraphTraversalSource g = neptuneClient.newTraversal();

        // 作者とのエッジを切る
        g.V(articleId).inE(UserToArticleEdge.PUBLISHED.name(), UserToArticleEdge.DRAFTED.name()).from(g.V(userId)).drop();

        // 論理削除
        long now = System.currentTimeMillis() / 1000L;
        g.V(userId)
         .addE(UserToArticleEdge.DELETED.name())
         .to(g.V(articleId))
         .property(UserToArticleEdge.DELETED.name(), now)
         .next();

        // TODO: Algoliaからデータを削除する
    }
}
