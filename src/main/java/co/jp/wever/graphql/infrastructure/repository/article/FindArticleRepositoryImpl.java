package co.jp.wever.graphql.infrastructure.repository.article;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.WithOptions;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import co.jp.wever.graphql.domain.repository.article.FindArticleRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.edge.label.ArticleToTagEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToArticleEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToTagEdge;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.converter.entity.article.ArticleDetailEntityConverter;
import co.jp.wever.graphql.infrastructure.datamodel.article.aggregation.ArticleDetailEntity;
import co.jp.wever.graphql.infrastructure.datamodel.article.aggregation.ArticleOutlineEntity;

@Component
public class FindArticleRepositoryImpl implements FindArticleRepository {

    private final NeptuneClient neptuneClient;

    public FindArticleRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    @Override
    public ArticleDetailEntity findOne(String articleId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        Map<String, Object> allResult = g.V(articleId)
                                         .project("base", "tags", "author", "status", "action", "liked", "learned")
                                         .by(__.valueMap().with(WithOptions.tokens))
                                         .by(__.out(ArticleToTagEdge.RELATED.getString())
                                               .hasLabel(VertexLabel.TAG.getString())
                                               .valueMap()
                                               .with(WithOptions.tokens)
                                               .fold())
                                         .by(__.in(UserToArticleEdge.DRAFTED.getString(),
                                                   UserToArticleEdge.DELETED.getString(),
                                                   UserToArticleEdge.PUBLISHED.getString())
                                               .hasLabel(VertexLabel.USER.getString())
                                               .project("base", "tags")
                                               .by(__.valueMap().with(WithOptions.tokens))
                                               .by(__.out(UserToTagEdge.RELATED.getString())
                                                     .hasLabel(VertexLabel.TAG.getString())
                                                     .valueMap()
                                                     .with(WithOptions.tokens)
                                                     .fold()))
                                         .by(__.inE(UserToArticleEdge.DRAFTED.getString(),
                                                    UserToArticleEdge.DELETED.getString(),
                                                    UserToArticleEdge.PUBLISHED.getString())
                                               .label()) // TODO: .fold()つけて複数の状態管理になっていないかチェックしたい
                                         .by(__.inE(UserToArticleEdge.LEARNED.getString(),
                                                    UserToArticleEdge.LIKED.getString()).label().fold())
                                         .by(__.in(UserToArticleEdge.LIKED.getString()).count())
                                         .by(__.in(UserToArticleEdge.LEARNED.getString()).count())
                                         .next();

        return ArticleDetailEntityConverter.toArticleDetailEntity(allResult);
    }

    @Override
    public List<ArticleOutlineEntity> findAll(String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        //TODO: このクエリがどうなるか確認
        // OKなら下の個別のクエリは不要
        List<Map<String, Object>> allResult = g.V(userId)
                                               .out()
                                               .hasLabel(VertexLabel.ARTICLE.getString())
                                               .project("base", "tag", "author", "like", "learned")
                                               .by(__.valueMap().with(WithOptions.tokens))
                                               .by(__.out(ArticleToTagEdge.RELATED.getString())
                                                     .hasLabel(VertexLabel.TAG.getString())
                                                     .valueMap()
                                                     .with(WithOptions.tokens))
                                               .by(__.in(UserToArticleEdge.DRAFTED.getString(),
                                                         UserToArticleEdge.PUBLISHED.getString())
                                                     .hasLabel(VertexLabel.USER.getString())
                                                     .valueMap()
                                                     .with(WithOptions.tokens))
                                               .by(__.out(UserToArticleEdge.LIKED.getString()).count())
                                               .by(__.out(UserToArticleEdge.LEARNED.getString()).count())
                                               .toList();

        //        List<Map<Object, Object>> articleResults =
        //            g.V(userId).out().hasLabel(VertexLabel.ARTICLE.getString()).valueMap().with(WithOptions.tokens).toList();
        //
        //        List<Map<Object, Object>> tagResults = g.V(articleId)
        //                                                .out(ArticleToTagEdge.RELATED.getString())
        //                                                .hasLabel(VertexLabel.TAG.getString())
        //                                                .valueMap()
        //                                                .with(WithOptions.tokens)
        //                                                .toList();
        //
        //        // TODO: ↓のクエリがラベルを取得できるかチェックする
        //        Map<Object, Object> authorResult = g.V(articleId)
        //                                            .in(UserToArticleEdge.DRAFTED.getString(), UserToArticleEdge.PUBLISHED.getString())
        //                                            .hasLabel(VertexLabel.USER.getString())
        //                                            .valueMap()
        //                                            .with(WithOptions.tokens)
        //                                            .next();
        //
        //        long like = g.V(articleId).out(UserToArticleEdge.LIKE.getString()).count().next();
        //        long bookmark = g.V(articleId).out(UserToArticleEdge.LEARNED.getString()).count().next();
        //
        //        //TODO Neptuneの動きを見て↑を最適化する
        //        //TODO クエリの時間とか
        //        return ArticleDetailEntityConverter.toArticleDetailEntity(articleResult,
        //                                                                  tagResults,
        //                                                                  authorResult,
        //                                                                  like,
        //                                                                  bookmark);
        return null;
    }

    @Override
    public List<ArticleOutlineEntity> findAllPublished(String userId) {
        return null;
    }

    @Override
    public List<ArticleOutlineEntity> findAllDrafted(String userId) {
        return null;
    }

    @Override
    public List<ArticleOutlineEntity> findAllLiked(String userId) {
        return null;
    }

    @Override
    public List<ArticleOutlineEntity> findAllLearned(String userId) {
        return null;
    }

    @Override
    public List<ArticleOutlineEntity> findAllBookmarked(String userId) {
        return null;
    }

    @Override
    public List<ArticleOutlineEntity> findFamous() {
        return null;
    }

    public List<ArticleOutlineEntity> findRelated(String userId) {
        return null;
    }
}
