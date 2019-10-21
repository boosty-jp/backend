package co.jp.wever.graphql.infrastructure.repository.section;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.WithOptions;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import co.jp.wever.graphql.domain.repository.section.FindSectionRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.edge.label.ArticleToSectionEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.ArticleToTagEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToArticleEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToSectionEdge;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.converter.entity.article.ArticleDetailEntityConverter;
import co.jp.wever.graphql.infrastructure.datamodel.section.SectionEntity;

@Component
public class FindSectionRepositoryImpl implements FindSectionRepository {

    private final NeptuneClient neptuneClient;

    public FindSectionRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    @Override
    public SectionEntity findOne(String sectionId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        //TODO: このクエリがどうなるか確認
        // OKなら下の個別のクエリは不要
        Map<String, Object> allResult = g.V(sectionId)
                                         .project("base", "author", "article", "like")
                                         .by(__.valueMap().with(WithOptions.tokens))
                                         .by(__.in(UserToSectionEdge.CREATED.name())
                                               .valueMap()
                                               .with(WithOptions.tokens))
                                         .by(__.in(ArticleToSectionEdge.INCLUDE.name())
                                               .hasLabel(VertexLabel.ARTICLE.name())
                                               .valueMap()
                                               .with(WithOptions.tokens))
                                         .by(__.in(UserToSectionEdge.LIKE.name()).count())
                                         .next();

        //        Map<Object, Object> articleResult = g.V(articleId).valueMap().with(WithOptions.tokens).next();
        //
        //        List<Map<Object, Object>> tagResults = g.V(articleId)
        //                                                .out(ArticleToTagEdge.RELATED.name())
        //                                                .hasLabel(VertexLabel.TAG.name())
        //                                                .valueMap()
        //                                                .with(WithOptions.tokens)
        //                                                .toList();
        //
        //        // TODO: ↓のクエリがラベルを取得できるかチェックする
        //        Map<Object, Object> authorResult = g.V(articleId)
        //                                            .in(UserToArticleEdge.DRAFTED.name(), UserToArticleEdge.PUBLISHED.name())
        //                                            .hasLabel(VertexLabel.USER.name())
        //                                            .valueMap()
        //                                            .with(WithOptions.tokens)
        //                                            .next();
        //
        //        long like = g.V(articleId).out(UserToArticleEdge.LIKE.name()).count().next();
        //        long bookmark = g.V(articleId).out(UserToArticleEdge.LEARNED.name()).count().next();
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
    public List<SectionEntity> findAllOnArticle(String articleId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<String, Object>> allResult = g.V(articleId)
                                               .out(ArticleToSectionEdge.INCLUDE.name())
                                               .hasLabel(VertexLabel.ARTICLE.name())
                                               .project("base", "like")
                                               .by(__.valueMap().with(WithOptions.tokens))
                                               .by(__.in(UserToArticleEdge.LIKE.name()).count())
                                               .toList();

        return null;
    }

    @Override
    public List<SectionEntity> findAllLiked(String userId) {
        return null;
    }

    @Override
    public List<SectionEntity> findAllBookmarked(String userId) {
        return null;
    }

    @Override
    public List<SectionEntity> findFamous() {
        return null;
    }

    @Override
    public List<SectionEntity> findRelated(String userId) {
        return null;
    }

}
