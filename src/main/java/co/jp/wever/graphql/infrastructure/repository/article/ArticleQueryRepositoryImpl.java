package co.jp.wever.graphql.infrastructure.repository.article;

import org.apache.tinkerpop.gremlin.process.traversal.Order;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.WithOptions;
import org.apache.tinkerpop.gremlin.structure.T;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.domainmodel.search.SearchCondition;
import co.jp.wever.graphql.domain.repository.article.ArticleQueryRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.edge.EdgeLabel;
import co.jp.wever.graphql.infrastructure.constant.edge.property.IncludeEdgeProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.DateProperty;
import co.jp.wever.graphql.infrastructure.converter.entity.article.ArticleEntityConverter;
import co.jp.wever.graphql.infrastructure.datamodel.article.ArticleEntity;
import co.jp.wever.graphql.infrastructure.datamodel.article.ArticleListEntity;
import co.jp.wever.graphql.infrastructure.util.EdgeIdCreator;

import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.constant;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.inE;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.inV;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.outV;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.unfold;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.valueMap;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.coalesce;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.select;

@Component
public class ArticleQueryRepositoryImpl implements ArticleQueryRepository {

    private final NeptuneClient neptuneClient;

    public ArticleQueryRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    @Override
    public ArticleEntity findOne(String articleId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        Map<String, Object> allResult = g.V(articleId)
                                         .hasLabel(VertexLabel.ARTICLE.getString())
                                         .project("base",
                                                  "blocks",
                                                  "tags",
                                                  "skills",
                                                  "author",
                                                  "status",
                                                  "userLiked",
                                                  "userLearned",
                                                  "liked",
                                                  "learned")
                                         .by(__.valueMap().with(WithOptions.tokens))
                                         .by(__.out(EdgeLabel.INCLUDE.getString())
                                               .hasLabel(VertexLabel.ARTICLE_TEXT.getString())
                                               .out(EdgeLabel.INCLUDE.getString())
                                               .hasLabel(VertexLabel.ARTICLE_BLOCK.getString())
                                               .order()
                                               .by(__.inE(EdgeLabel.INCLUDE.getString())
                                                     .values(IncludeEdgeProperty.NUMBER.getString()), Order.asc)
                                               .valueMap()
                                               .fold())
                                         .by(__.out(EdgeLabel.RELATED_TO.getString())
                                               .hasLabel(VertexLabel.TAG.getString())
                                               .valueMap()
                                               .with(WithOptions.tokens)
                                               .fold())
                                         .by(__.outE(EdgeLabel.TEACH.getString())
                                               .where(inV().hasLabel(VertexLabel.SKILL.getString()))
                                               .as("teachEdge")
                                               .inV()
                                               .as("skillVertex")
                                               .select("teachEdge", "skillVertex")
                                               .by(valueMap())
                                               .fold())
                                         .by(__.in(EdgeLabel.DRAFT.getString(),
                                                   EdgeLabel.DELETE.getString(),
                                                   EdgeLabel.PUBLISH.getString())
                                               .hasLabel(VertexLabel.USER.getString())
                                               .valueMap()
                                               .with(WithOptions.tokens))
                                         .by(__.inE(EdgeLabel.DRAFT.getString(),
                                                    EdgeLabel.DELETE.getString(),
                                                    EdgeLabel.PUBLISH.getString()).label())
                                         .by(coalesce(__.inE(EdgeLabel.LIKE.getString())
                                                        .where(outV().hasId(userId)
                                                                     .hasLabel(VertexLabel.USER.getString()))
                                                        .limit(1)
                                                        .constant(true), constant(false)))
                                         .by(coalesce(__.inE(EdgeLabel.LEARN.getString())
                                                        .where(outV().hasId(userId)
                                                                     .hasLabel(VertexLabel.USER.getString()))
                                                        .limit(1)
                                                        .constant(true), constant(false)))
                                         .by(__.in(EdgeLabel.LIKE.getString()).count())
                                         .by(__.in(EdgeLabel.LEARN.getString()).count())
                                         .next();

        g.E(EdgeIdCreator.createId(userId, articleId, EdgeLabel.VIEW.getString()))
         .hasLabel(EdgeLabel.VIEW.getString())
         .fold()
         .coalesce(unfold(),
                   g.V(userId)
                    .hasLabel(VertexLabel.USER.getString())
                    .addE(EdgeLabel.VIEW.getString())
                    .property(T.id, EdgeIdCreator.createId(userId, articleId, EdgeLabel.VIEW.getString()))
                    .property(DateProperty.CREATE_TIME.getString(), now)
                    .property(DateProperty.UPDATE_TIME.getString(), now)
                    .to(g.V(articleId).hasLabel(VertexLabel.ARTICLE.getString())))
         .next();

        //TODO: ↑のクエリに合わせる
        g.E(EdgeIdCreator.createId(userId, articleId, EdgeLabel.VIEW.getString()))
         .hasLabel(EdgeLabel.VIEW.getString())
         .property(DateProperty.UPDATE_TIME.getString(), now)
         .next();

        return ArticleEntityConverter.toArticleEntity(allResult);
    }

    @Override
    public ArticleEntity findOneForGuest(String articleId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        Map<String, Object> allResult = g.V(articleId)
                                         .project("base",
                                                  "blocks",
                                                  "tags",
                                                  "skills",
                                                  "author",
                                                  "status",
                                                  "liked",
                                                  "learned")
                                         .by(__.valueMap().with(WithOptions.tokens))
                                         .by(__.out(EdgeLabel.INCLUDE.getString())
                                               .hasLabel(VertexLabel.ARTICLE_TEXT.getString())
                                               .out(EdgeLabel.INCLUDE.getString())
                                               .hasLabel(VertexLabel.ARTICLE_BLOCK.getString())
                                               .valueMap()
                                               .fold())
                                         .by(__.out(EdgeLabel.RELATED_TO.getString())
                                               .hasLabel(VertexLabel.TAG.getString())
                                               .valueMap()
                                               .with(WithOptions.tokens)
                                               .fold())
                                         .by(__.outE(EdgeLabel.TEACH.getString())
                                               .hasLabel(VertexLabel.SKILL.getString())
                                               .as("teachEdge")
                                               .inV()
                                               .as("skillVertex")
                                               .select("teachEdge", "skillVertex")
                                               .by(valueMap())
                                               .fold())
                                         .by(__.in(EdgeLabel.DRAFT.getString(),
                                                   EdgeLabel.DELETE.getString(),
                                                   EdgeLabel.PUBLISH.getString())
                                               .hasLabel(VertexLabel.USER.getString())
                                               .valueMap()
                                               .with(WithOptions.tokens))
                                         .by(__.inE(EdgeLabel.DRAFT.getString(),
                                                    EdgeLabel.DELETE.getString(),
                                                    EdgeLabel.PUBLISH.getString()).label())
                                         .by(__.in(EdgeLabel.LIKE.getString()).count())
                                         .by(__.in(EdgeLabel.LEARN.getString()).count())
                                         .next();

        return ArticleEntityConverter.toArticleEntityForGuest(allResult);
    }

    @Override
    public ArticleListEntity findCreated(String userId, SearchCondition searchCondition) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        List<Map<String, Object>> allResults;
        long sumCount = findPublishedSumCount(g, userId);
        if (searchCondition.shouldSort()) {
            Order orderType = searchCondition.isAscend() ? Order.asc : Order.desc;
            if (searchCondition.vertexSort()) {
                allResults = g.V(userId)
                              .out(EdgeLabel.PUBLISH.getString())
                              .hasLabel(VertexLabel.ARTICLE.getString())
                              .order()
                              .by(searchCondition.getField(), orderType)
                              .range(searchCondition.getRangeStart(), searchCondition.getRangeEnd())
                              .project("base", "tags", "skills", "liked", "learned")
                              .by(__.valueMap().with(WithOptions.tokens))
                              .by(__.out(EdgeLabel.RELATED_TO.getString())
                                    .hasLabel(VertexLabel.TAG.getString())
                                    .valueMap()
                                    .with(WithOptions.tokens)
                                    .fold())
                              .by(__.outE(EdgeLabel.TEACH.getString())
                                    .hasLabel(VertexLabel.SKILL.getString())
                                    .as("teachEdge")
                                    .inV()
                                    .as("skillVertex")
                                    .select("teachEdge", "skillVertex")
                                    .by(valueMap())
                                    .fold())
                              .by(__.in(EdgeLabel.LIKE.getString()).count())
                              .by(__.in(EdgeLabel.LEARN.getString()).count())
                              .toList();
            } else {
                allResults = g.V(userId)
                              .out(EdgeLabel.PUBLISH.getString())
                              .hasLabel(VertexLabel.ARTICLE.getString())
                              .project("base", "tags", "skills", "liked", "learned", "sortEdge")
                              .by(__.valueMap().with(WithOptions.tokens))
                              .by(__.out(EdgeLabel.RELATED_TO.getString())
                                    .hasLabel(VertexLabel.TAG.getString())
                                    .valueMap()
                                    .with(WithOptions.tokens)
                                    .fold())
                              .by(__.outE(EdgeLabel.TEACH.getString())
                                    .hasLabel(VertexLabel.SKILL.getString())
                                    .as("teachEdge")
                                    .inV()
                                    .as("skillVertex")
                                    .select("teachEdge", "skillVertex")
                                    .by(valueMap())
                                    .fold())
                              .by(__.in(EdgeLabel.LIKE.getString()).count())
                              .by(__.in(EdgeLabel.LEARN.getString()).count())
                              .by(__.in(searchCondition.getField()).count())
                              .order()
                              .by(select("sortEdge"), orderType)
                              .range(searchCondition.getRangeStart(), searchCondition.getRangeEnd())
                              .toList();
            }
        } else {
            allResults = g.V(userId)
                          .out(EdgeLabel.PUBLISH.getString())
                          .hasLabel(VertexLabel.ARTICLE.getString())
                          .range(searchCondition.getRangeStart(), searchCondition.getRangeEnd())
                          .project("base", "tags", "skills", "liked", "learned")
                          .by(__.valueMap().with(WithOptions.tokens))
                          .by(__.out(EdgeLabel.RELATED_TO.getString())
                                .hasLabel(VertexLabel.TAG.getString())
                                .valueMap()
                                .with(WithOptions.tokens)
                                .fold())
                          .by(__.outE(EdgeLabel.TEACH.getString())
                                .hasLabel(VertexLabel.SKILL.getString())
                                .as("teachEdge")
                                .inV()
                                .as("skillVertex")
                                .select("teachEdge", "skillVertex")
                                .by(valueMap())
                                .fold())
                          .by(__.in(EdgeLabel.LIKE.getString()).count())
                          .by(__.in(EdgeLabel.LEARN.getString()).count())
                          .toList();
        }

        return ArticleListEntity.builder()
                                .articles(allResults.stream()
                                                    .map(r -> ArticleEntityConverter.toArticleEntityForList(r))
                                                    .collect(Collectors.toList()))
                                .sumCount(sumCount)
                                .build();
    }

    @Override
    public ArticleListEntity findCreatedBySelf(String userId, SearchCondition searchCondition) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        List<Map<String, Object>> allResults;
        long sumCount = 0;
        if (searchCondition.shouldFilter()) {
            allResults = findCreatedBySelfWithFilter(g, userId, searchCondition);
            sumCount = findSumCountWithFilter(g, userId, searchCondition);
        } else {
            allResults = findCreatedBySelfWithoutFilter(g, userId, searchCondition);
            sumCount = findSumCountWithoutFilter(g, userId);
        }

        return ArticleListEntity.builder()
                                .articles(allResults.stream()
                                                    .map(r -> ArticleEntityConverter.toArticleEntityForListWithStatus(r))
                                                    .collect(Collectors.toList()))
                                .sumCount(sumCount)
                                .build();
    }

    private long findSumCountWithFilter(GraphTraversalSource g, String userId, SearchCondition searchCondition) {
        return g.V(userId).out(searchCondition.getFilter()).hasLabel(VertexLabel.ARTICLE.getString()).count().next();
    }

    private long findSumCountWithoutFilter(GraphTraversalSource g, String userId) {
        return g.V(userId)
                .out(EdgeLabel.DRAFT.getString(), EdgeLabel.PUBLISH.getString())
                .hasLabel(VertexLabel.ARTICLE.getString())
                .count()
                .next();
    }

    private long findPublishedSumCount(GraphTraversalSource g, String userId) {
        return g.V(userId).out(EdgeLabel.PUBLISH.getString()).hasLabel(VertexLabel.ARTICLE.getString()).count().next();
    }

    private List<Map<String, Object>> findCreatedBySelfWithFilter(
        GraphTraversalSource g, String userId, SearchCondition searchCondition) {

        if (searchCondition.shouldSort()) {
            Order orderType = searchCondition.isAscend() ? Order.asc : Order.desc;
            if (searchCondition.vertexSort()) {
                return g.V(userId)
                        .out(searchCondition.getFilter())
                        .hasLabel(VertexLabel.ARTICLE.getString())
                        .order()
                        .by(searchCondition.getField(), orderType)
                        .range(searchCondition.getRangeStart(), searchCondition.getRangeEnd())
                        .project("base", "tags", "skills", "status", "liked", "learned")
                        .by(__.valueMap().with(WithOptions.tokens))
                        .by(__.out(EdgeLabel.RELATED_TO.getString())
                              .hasLabel(VertexLabel.TAG.getString())
                              .valueMap()
                              .with(WithOptions.tokens)
                              .fold())
                        .by(__.outE(EdgeLabel.TEACH.getString())
                              .hasLabel(VertexLabel.SKILL.getString())
                              .as("teachEdge")
                              .inV()
                              .as("skillVertex")
                              .select("teachEdge", "skillVertex")
                              .by(valueMap())
                              .fold())
                        .by(__.inE(searchCondition.getFilter()).label())
                        .by(__.in(EdgeLabel.LIKE.getString()).count())
                        .by(__.in(EdgeLabel.LEARN.getString()).count())
                        .toList();
            } else {
                return g.V(userId)
                        .out(searchCondition.getFilter())
                        .hasLabel(VertexLabel.ARTICLE.getString())
                        .project("base", "tags", "skills", "status", "liked", "learned", "sortEdge")
                        .by(__.valueMap().with(WithOptions.tokens))
                        .by(__.out(EdgeLabel.RELATED_TO.getString())
                              .hasLabel(VertexLabel.TAG.getString())
                              .valueMap()
                              .with(WithOptions.tokens)
                              .fold())
                        .by(__.outE(EdgeLabel.TEACH.getString())
                              .hasLabel(VertexLabel.SKILL.getString())
                              .as("teachEdge")
                              .inV()
                              .as("skillVertex")
                              .select("teachEdge", "skillVertex")
                              .by(valueMap())
                              .fold())
                        .by(__.inE(searchCondition.getFilter()).label())
                        .by(__.in(EdgeLabel.LIKE.getString()).count())
                        .by(__.in(EdgeLabel.LEARN.getString()).count())
                        .by(__.in(searchCondition.getField()).count())
                        .order()
                        .by(select("sortEdge"), orderType)
                        .range(searchCondition.getRangeStart(), searchCondition.getRangeEnd())
                        .toList();
            }
        } else {
            return g.V(userId)
                    .out(searchCondition.getFilter())
                    .hasLabel(VertexLabel.ARTICLE.getString())
                    .range(searchCondition.getRangeStart(), searchCondition.getRangeEnd())
                    .project("base", "tags", "skills", "status", "liked", "learned")
                    .by(__.valueMap().with(WithOptions.tokens))
                    .by(__.out(EdgeLabel.RELATED_TO.getString())
                          .hasLabel(VertexLabel.TAG.getString())
                          .valueMap()
                          .with(WithOptions.tokens)
                          .fold())
                    .by(__.outE(EdgeLabel.TEACH.getString())
                          .hasLabel(VertexLabel.SKILL.getString())
                          .as("teachEdge")
                          .inV()
                          .as("skillVertex")
                          .select("teachEdge", "skillVertex")
                          .by(valueMap())
                          .fold())
                    .by(__.inE(searchCondition.getFilter()).label())
                    .by(__.in(EdgeLabel.LIKE.getString()).count())
                    .by(__.in(EdgeLabel.LEARN.getString()).count())
                    .toList();
        }
    }

    private List<Map<String, Object>> findCreatedBySelfWithoutFilter(
        GraphTraversalSource g, String userId, SearchCondition searchCondition) {
        if (searchCondition.shouldSort()) {
            Order orderType = searchCondition.isAscend() ? Order.asc : Order.desc;
            if (searchCondition.vertexSort()) {
                return g.V(userId)
                        .out(EdgeLabel.DRAFT.getString(), EdgeLabel.PUBLISH.getString())
                        .hasLabel(VertexLabel.ARTICLE.getString())
                        .order()
                        .by(searchCondition.getField(), orderType)
                        .range(searchCondition.getRangeStart(), searchCondition.getRangeEnd())
                        .project("base", "tags", "skills", "status", "liked", "learned")
                        .by(__.valueMap().with(WithOptions.tokens))
                        .by(__.out(EdgeLabel.RELATED_TO.getString())
                              .hasLabel(VertexLabel.TAG.getString())
                              .valueMap()
                              .with(WithOptions.tokens)
                              .fold())
                        .by(__.outE(EdgeLabel.TEACH.getString())
                              .hasLabel(VertexLabel.SKILL.getString())
                              .as("teachEdge")
                              .inV()
                              .as("skillVertex")
                              .select("teachEdge", "skillVertex")
                              .by(valueMap())
                              .fold())
                        .by(__.inE(EdgeLabel.DRAFT.getString(), EdgeLabel.PUBLISH.getString()).label())
                        .by(__.in(EdgeLabel.LIKE.getString()).count())
                        .by(__.in(EdgeLabel.LEARN.getString()).count())
                        .toList();
            } else {
                return g.V(userId)
                        .out(EdgeLabel.DRAFT.getString(), EdgeLabel.PUBLISH.getString())
                        .hasLabel(VertexLabel.ARTICLE.getString())
                        .project("base", "tags", "skills", "status", "liked", "learned", "sortEdge")
                        .by(__.valueMap().with(WithOptions.tokens))
                        .by(__.out(EdgeLabel.RELATED_TO.getString())
                              .hasLabel(VertexLabel.TAG.getString())
                              .valueMap()
                              .with(WithOptions.tokens)
                              .fold())
                        .by(__.outE(EdgeLabel.TEACH.getString())
                              .hasLabel(VertexLabel.SKILL.getString())
                              .as("teachEdge")
                              .inV()
                              .as("skillVertex")
                              .select("teachEdge", "skillVertex")
                              .by(valueMap())
                              .fold())
                        .by(__.inE(EdgeLabel.DRAFT.getString(), EdgeLabel.PUBLISH.getString()).label())
                        .by(__.in(EdgeLabel.LIKE.getString()).count())
                        .by(__.in(EdgeLabel.LEARN.getString()).count())
                        .by(__.in(searchCondition.getField()).count())
                        .order()
                        .by(select("sortEdge"), orderType)
                        .range(searchCondition.getRangeStart(), searchCondition.getRangeEnd())
                        .toList();
            }
        } else {
            return g.V(userId)
                    .out(EdgeLabel.DRAFT.getString(), EdgeLabel.PUBLISH.getString())
                    .hasLabel(VertexLabel.ARTICLE.getString())
                    .range(searchCondition.getRangeStart(), searchCondition.getRangeEnd())
                    .project("base", "tags", "skills", "status", "liked", "learned")
                    .by(__.valueMap().with(WithOptions.tokens))
                    .by(__.out(EdgeLabel.RELATED_TO.getString())
                          .hasLabel(VertexLabel.TAG.getString())
                          .valueMap()
                          .with(WithOptions.tokens)
                          .fold())
                    .by(__.outE(EdgeLabel.TEACH.getString())
                          .hasLabel(VertexLabel.SKILL.getString())
                          .as("teachEdge")
                          .inV()
                          .as("skillVertex")
                          .select("teachEdge", "skillVertex")
                          .by(valueMap())
                          .fold())
                    .by(__.inE(EdgeLabel.DRAFT.getString(), EdgeLabel.PUBLISH.getString()).label())
                    .by(__.in(EdgeLabel.LIKE.getString()).count())
                    .by(__.in(EdgeLabel.LEARN.getString()).count())
                    .toList();
        }
    }

    @Override
    public ArticleListEntity findActioned(String userId, SearchCondition searchCondition) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        List<Map<String, Object>> allResults;
        long sumCount = findSumCountWithFilter(g, userId, searchCondition);
        if (searchCondition.shouldSort()) {
            Order orderType = searchCondition.isAscend() ? Order.asc : Order.desc;
            if (searchCondition.vertexSort()) {
                allResults = g.V(userId)
                              .out(searchCondition.getFilter())
                              .hasLabel(VertexLabel.ARTICLE.getString())
                              .order()
                              .by(searchCondition.getField(), orderType)
                              .range(searchCondition.getRangeStart(), searchCondition.getRangeEnd())
                              .project("base", "tags", "skills", "status", "liked", "learned")
                              .by(__.valueMap().with(WithOptions.tokens))
                              .by(__.out(EdgeLabel.RELATED_TO.getString())
                                    .hasLabel(VertexLabel.TAG.getString())
                                    .valueMap()
                                    .with(WithOptions.tokens)
                                    .fold())
                              .by(__.outE(EdgeLabel.TEACH.getString())
                                    .hasLabel(VertexLabel.SKILL.getString())
                                    .as("teachEdge")
                                    .inV()
                                    .as("skillVertex")
                                    .select("teachEdge", "skillVertex")
                                    .by(valueMap())
                                    .fold())
                              .by(__.inE(EdgeLabel.DRAFT.getString(),
                                         EdgeLabel.PUBLISH.getString(),
                                         EdgeLabel.DELETE.getString()).label())
                              .by(__.in(EdgeLabel.LIKE.getString()).count())
                              .by(__.in(EdgeLabel.LEARN.getString()).count())
                              .toList();
            } else {
                allResults = g.V(userId)
                              .out(searchCondition.getFilter())
                              .hasLabel(VertexLabel.ARTICLE.getString())
                              .project("base", "tags", "skills", "status", "liked", "learned", "sortEdge")
                              .by(__.valueMap().with(WithOptions.tokens))
                              .by(__.out(EdgeLabel.RELATED_TO.getString())
                                    .hasLabel(VertexLabel.TAG.getString())
                                    .valueMap()
                                    .with(WithOptions.tokens)
                                    .fold())
                              .by(__.outE(EdgeLabel.TEACH.getString())
                                    .hasLabel(VertexLabel.SKILL.getString())
                                    .as("teachEdge")
                                    .inV()
                                    .as("skillVertex")
                                    .select("teachEdge", "skillVertex")
                                    .by(valueMap())
                                    .fold())
                              .by(__.inE(EdgeLabel.DRAFT.getString(),
                                         EdgeLabel.PUBLISH.getString(),
                                         EdgeLabel.DELETE.getString()).label())
                              .by(__.in(EdgeLabel.LIKE.getString()).count())
                              .by(__.in(EdgeLabel.LEARN.getString()).count())
                              .by(__.in(searchCondition.getField()).count())
                              .order()
                              .by(select("sortEdge"), orderType)
                              .range(searchCondition.getRangeStart(), searchCondition.getRangeEnd())
                              .toList();
            }
        } else {
            allResults = g.V(userId)
                          .out(searchCondition.getFilter())
                          .hasLabel(VertexLabel.ARTICLE.getString())
                          .range(searchCondition.getRangeStart(), searchCondition.getRangeEnd())
                          .project("base", "tags", "skills", "status", "liked", "learned")
                          .by(__.valueMap().with(WithOptions.tokens))
                          .by(__.out(EdgeLabel.RELATED_TO.getString())
                                .hasLabel(VertexLabel.TAG.getString())
                                .valueMap()
                                .with(WithOptions.tokens)
                                .fold())
                          .by(__.outE(EdgeLabel.TEACH.getString())
                                .hasLabel(VertexLabel.SKILL.getString())
                                .as("teachEdge")
                                .inV()
                                .as("skillVertex")
                                .select("teachEdge", "skillVertex")
                                .by(valueMap())
                                .fold())
                          .by(__.inE(EdgeLabel.DRAFT.getString(),
                                     EdgeLabel.PUBLISH.getString(),
                                     EdgeLabel.DELETE.getString()).label())
                          .by(__.in(EdgeLabel.LIKE.getString()).count())
                          .by(__.in(EdgeLabel.LEARN.getString()).count())
                          .toList();
        }

        return ArticleListEntity.builder()
                                .articles(allResults.stream()
                                                    .map(r -> ArticleEntityConverter.toArticleEntityForListWithStatus(r))
                                                    .collect(Collectors.toList()))
                                .sumCount(sumCount)
                                .build();
    }

    @Override
    public List<ArticleEntity> findFamous() {
        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<String, Object>> allResults = g.V()
                                                .hasLabel(VertexLabel.ARTICLE.getString())
                                                .filter(inE().hasLabel(EdgeLabel.PUBLISH.getString()))
                                                .project("base", "tags", "author", "skills", "liked", "learned")
                                                .by(__.valueMap().with(WithOptions.tokens))
                                                .by(__.out(EdgeLabel.RELATED_TO.getString())
                                                      .hasLabel(VertexLabel.TAG.getString())
                                                      .valueMap()
                                                      .with(WithOptions.tokens)
                                                      .fold())
                                                .by(__.in(EdgeLabel.PUBLISH.getString())
                                                      .hasLabel(VertexLabel.USER.getString())
                                                      .valueMap()
                                                      .with(WithOptions.tokens))
                                                .by(__.outE(EdgeLabel.TEACH.getString())
                                                      .hasLabel(VertexLabel.SKILL.getString())
                                                      .as("teachEdge")
                                                      .inV()
                                                      .as("skillVertex")
                                                      .select("teachEdge", "skillVertex")
                                                      .by(valueMap())
                                                      .fold())
                                                .by(__.in(EdgeLabel.LIKE.getString()).count())
                                                .by(__.in(EdgeLabel.LEARN.getString()).count())
                                                .order()
                                                .by(select("learned"), Order.desc) //TODO: learnedとlikedの合計にしたい
                                                .limit(10)
                                                .toList();

        return allResults.stream()
                         .map(r -> ArticleEntityConverter.toArticleEntityForList(r))
                         .collect(Collectors.toList());
    }

    @Override
    public String findAuthorId(String articleId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        return g.V(articleId)
                .hasLabel(VertexLabel.ARTICLE.getString())
                .in(EdgeLabel.PUBLISH.getString(), EdgeLabel.DRAFT.getString())
                .hasLabel(VertexLabel.USER.getString())
                .id()
                .next()
                .toString();
    }

    @Override
    public int publishedArticleCount(List<String> articleIds) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        return g.V(articleIds)
                .hasLabel(VertexLabel.ARTICLE.getString())
                .in(EdgeLabel.PUBLISH.getString())
                .id()
                .toList()
                .size();
    }
}
