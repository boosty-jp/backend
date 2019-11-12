package co.jp.wever.graphql.infrastructure.repository.article;

import org.apache.tinkerpop.gremlin.process.traversal.Order;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.WithOptions;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.repository.article.FindArticleRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.edge.label.ArticleToTagEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToArticleEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToTagEdge;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.ArticleVertexProperty;
import co.jp.wever.graphql.infrastructure.converter.entity.article.ArticleDetailEntityConverter;
import co.jp.wever.graphql.infrastructure.datamodel.article.aggregation.ArticleDetailEntity;

import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.inE;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.values;

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
                                                    UserToArticleEdge.PUBLISHED.getString()).label())
                                         .by(__.inE(UserToArticleEdge.LEARNED.getString(),
                                                    UserToArticleEdge.LIKED.getString()).label().fold())
                                         .by(__.in(UserToArticleEdge.LIKED.getString()).count())
                                         .by(__.in(UserToArticleEdge.LEARNED.getString()).count())
                                         .next();

        return ArticleDetailEntityConverter.toArticleDetailEntity(allResult);
    }

    @Override
    public List<ArticleDetailEntity> findAll(String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        List<Map<String, Object>> allResults = g.V(userId)
                                                .out(UserToArticleEdge.PUBLISHED.getString(),
                                                     UserToArticleEdge.DRAFTED.getString())
                                                .hasLabel(VertexLabel.ARTICLE.getString())
                                                .project("base",
                                                         "tags",
                                                         "author",
                                                         "status",
                                                         "action",
                                                         "liked",
                                                         "learned")
                                                .by(__.valueMap().with(WithOptions.tokens))
                                                .by(__.out(ArticleToTagEdge.RELATED.getString())
                                                      .hasLabel(VertexLabel.TAG.getString())
                                                      .valueMap()
                                                      .with(WithOptions.tokens)
                                                      .fold())
                                                .by(__.in(UserToArticleEdge.PUBLISHED.getString(),
                                                          UserToArticleEdge.DRAFTED.getString())
                                                      .hasLabel(VertexLabel.USER.getString())
                                                      .project("base", "tags")
                                                      .by(__.valueMap().with(WithOptions.tokens))
                                                      .by(__.out(UserToTagEdge.RELATED.getString())
                                                            .hasLabel(VertexLabel.TAG.getString())
                                                            .valueMap()
                                                            .with(WithOptions.tokens)
                                                            .fold()))
                                                .by(__.inE(UserToArticleEdge.DRAFTED.getString(),
                                                           UserToArticleEdge.PUBLISHED.getString()).label())
                                                .by(__.inE(UserToArticleEdge.LEARNED.getString(),
                                                           UserToArticleEdge.LIKED.getString()).label().fold())
                                                .by(__.in(UserToArticleEdge.LIKED.getString()).count())
                                                .by(__.in(UserToArticleEdge.LEARNED.getString()).count())
                                                .toList();

        return allResults.stream()
                         .map(r -> ArticleDetailEntityConverter.toArticleDetailEntity(r))
                         .collect(Collectors.toList());
    }

    @Override
    public List<ArticleDetailEntity> findAllPublished(String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        List<Map<String, Object>> allResults = g.V(userId)
                                                .out(UserToArticleEdge.PUBLISHED.getString())
                                                .hasLabel(VertexLabel.ARTICLE.getString())
                                                .project("base",
                                                         "tags",
                                                         "author",
                                                         "status",
                                                         "action",
                                                         "liked",
                                                         "learned")
                                                .by(__.valueMap().with(WithOptions.tokens))
                                                .by(__.out(ArticleToTagEdge.RELATED.getString())
                                                      .hasLabel(VertexLabel.TAG.getString())
                                                      .valueMap()
                                                      .with(WithOptions.tokens)
                                                      .fold())
                                                .by(__.in(UserToArticleEdge.PUBLISHED.getString())
                                                      .hasLabel(VertexLabel.USER.getString())
                                                      .project("base", "tags")
                                                      .by(__.valueMap().with(WithOptions.tokens))
                                                      .by(__.out(UserToTagEdge.RELATED.getString())
                                                            .hasLabel(VertexLabel.TAG.getString())
                                                            .valueMap()
                                                            .with(WithOptions.tokens)
                                                            .fold()))
                                                .by(__.inE(UserToArticleEdge.PUBLISHED.getString())
                                                      .label()) // TODO: .fold()つけて複数の状態管理になっていないかチェックしたい
                                                .by(__.inE(UserToArticleEdge.LEARNED.getString(),
                                                           UserToArticleEdge.LIKED.getString()).label().fold())
                                                .by(__.in(UserToArticleEdge.LIKED.getString()).count())
                                                .by(__.in(UserToArticleEdge.LEARNED.getString()).count())
                                                .toList();

        return allResults.stream()
                         .map(r -> ArticleDetailEntityConverter.toArticleDetailEntity(r))
                         .collect(Collectors.toList());
    }

    @Override
    public List<ArticleDetailEntity> findAllDrafted(String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        List<Map<String, Object>> allResults = g.V(userId)
                                                .out(UserToArticleEdge.DRAFTED.getString())
                                                .hasLabel(VertexLabel.ARTICLE.getString())
                                                .project("base",
                                                         "tags",
                                                         "author",
                                                         "status",
                                                         "action",
                                                         "liked",
                                                         "learned")
                                                .by(__.valueMap().with(WithOptions.tokens))
                                                .by(__.out(ArticleToTagEdge.RELATED.getString())
                                                      .hasLabel(VertexLabel.TAG.getString())
                                                      .valueMap()
                                                      .with(WithOptions.tokens)
                                                      .fold())
                                                .by(__.in(UserToArticleEdge.DRAFTED.getString())
                                                      .hasLabel(VertexLabel.USER.getString())
                                                      .project("base", "tags")
                                                      .by(__.valueMap().with(WithOptions.tokens))
                                                      .by(__.out(UserToTagEdge.RELATED.getString())
                                                            .hasLabel(VertexLabel.TAG.getString())
                                                            .valueMap()
                                                            .with(WithOptions.tokens)
                                                            .fold()))
                                                .by(__.inE(UserToArticleEdge.DRAFTED.getString()).label())
                                                .by(__.inE(UserToArticleEdge.LEARNED.getString(),
                                                           UserToArticleEdge.LIKED.getString()).label().fold())
                                                .by(__.in(UserToArticleEdge.LIKED.getString()).count())
                                                .by(__.in(UserToArticleEdge.LEARNED.getString()).count())
                                                .toList();

        return allResults.stream()
                         .map(r -> ArticleDetailEntityConverter.toArticleDetailEntity(r))
                         .collect(Collectors.toList());
    }

    @Override
    public List<ArticleDetailEntity> findAllLiked(String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        List<Map<String, Object>> allResults = g.V(userId)
                                                .out(UserToArticleEdge.LIKED.getString())
                                                .hasLabel(VertexLabel.ARTICLE.getString())
                                                .project("base",
                                                         "tags",
                                                         "author",
                                                         "status",
                                                         "action",
                                                         "liked",
                                                         "learned")
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
                                                           UserToArticleEdge.PUBLISHED.getString()).label())
                                                .by(__.inE(UserToArticleEdge.LEARNED.getString(),
                                                           UserToArticleEdge.LIKED.getString()).label().fold())
                                                .by(__.in(UserToArticleEdge.LIKED.getString()).count())
                                                .by(__.in(UserToArticleEdge.LEARNED.getString()).count())
                                                .toList();

        return allResults.stream()
                         .map(r -> ArticleDetailEntityConverter.toArticleDetailEntity(r))
                         .collect(Collectors.toList());
    }

    @Override
    public List<ArticleDetailEntity> findAllLearned(String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<String, Object>> allResults = g.V(userId)
                                                .out(UserToArticleEdge.LEARNED.getString())
                                                .hasLabel(VertexLabel.ARTICLE.getString())
                                                .project("base",
                                                         "tags",
                                                         "author",
                                                         "status",
                                                         "action",
                                                         "liked",
                                                         "learned")
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
                                                           UserToArticleEdge.PUBLISHED.getString()).label())
                                                .by(__.inE(UserToArticleEdge.LEARNED.getString(),
                                                           UserToArticleEdge.LIKED.getString()).label().fold())
                                                .by(__.in(UserToArticleEdge.LIKED.getString()).count())
                                                .by(__.in(UserToArticleEdge.LEARNED.getString()).count())
                                                .toList();

        return allResults.stream()
                         .map(r -> ArticleDetailEntityConverter.toArticleDetailEntity(r))
                         .collect(Collectors.toList());
    }


    @Override
    public String findAuthorId(String articleId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        return (String) g.V(articleId)
                         .hasLabel(VertexLabel.ARTICLE.getString())
                         .in(UserToArticleEdge.PUBLISHED.getString(), UserToArticleEdge.DRAFTED.getString())
                         .id()
                         .next();
    }

    @Override
    public List<ArticleDetailEntity> findFamous() {
        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<String, Object>> allResults = g.V()
                                                .hasLabel(VertexLabel.ARTICLE.getString())
                                                .filter(inE().hasLabel(UserToArticleEdge.PUBLISHED.getString()))
                                                .order()
                                                .by(values(ArticleVertexProperty.LIKED.getString(),
                                                           ArticleVertexProperty.LEARNED.getString()).sum(), Order.desc)
                                                .limit(10)
                                                .project("base",
                                                         "tags",
                                                         "author",
                                                         "status",
                                                         "action",
                                                         "liked",
                                                         "learned")
                                                .by(__.valueMap().with(WithOptions.tokens))
                                                .by(__.out(ArticleToTagEdge.RELATED.getString())
                                                      .hasLabel(VertexLabel.TAG.getString())
                                                      .valueMap()
                                                      .with(WithOptions.tokens)
                                                      .fold())
                                                .by(__.in(UserToArticleEdge.PUBLISHED.getString(),
                                                          UserToArticleEdge.DELETED.getString(),
                                                          UserToArticleEdge.DRAFTED.getString())
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
                                                           UserToArticleEdge.PUBLISHED.getString()).label())
                                                .by(__.inE(UserToArticleEdge.LEARNED.getString(),
                                                           UserToArticleEdge.LIKED.getString()).label().fold())
                                                .by(__.in(UserToArticleEdge.LIKED.getString()).count())
                                                .by(__.in(UserToArticleEdge.LEARNED.getString()).count())
                                                .toList();

        return allResults.stream()
                         .map(r -> ArticleDetailEntityConverter.toArticleDetailEntity(r))
                         .collect(Collectors.toList());
    }

    public List<ArticleDetailEntity> findRelated(String userId) {
        return null;
    }
}
