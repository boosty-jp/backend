package co.jp.wever.graphql.infrastructure.repository.article;

import org.apache.tinkerpop.gremlin.process.traversal.Order;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.WithOptions;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.domainmodel.search.others.SearchCondition;
import co.jp.wever.graphql.domain.domainmodel.search.self.SelfSearchCondition;
import co.jp.wever.graphql.domain.repository.article.FindArticleRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.edge.label.ArticleToSkillEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.ArticleToTagEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToArticleEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToContentProperty;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToTagEdge;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.ArticleVertexProperty;
import co.jp.wever.graphql.infrastructure.converter.entity.article.ArticleEntityConverter;
import co.jp.wever.graphql.infrastructure.datamodel.article.ArticleEntity;
import co.jp.wever.graphql.infrastructure.datamodel.article.aggregation.ArticleDetailEntity;

import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.constant;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.inE;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.outV;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.valueMap;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.values;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.coalesce;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.select;

@Component
public class FindArticleRepositoryImpl implements FindArticleRepository {

    private final NeptuneClient neptuneClient;

    public FindArticleRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    @Override
    public ArticleEntity findOne(String articleId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        Map<String, Object> allResult = g.V(articleId)
                                         .project("base",
                                                  "tags",
                                                  "skills",
                                                  "author",
                                                  "status",
                                                  "userLiked",
                                                  "userLearned",
                                                  "liked",
                                                  "learned")
                                         .by(__.valueMap().with(WithOptions.tokens))
                                         .by(__.out(ArticleToTagEdge.RELATED.getString())
                                               .hasLabel(VertexLabel.TAG.getString())
                                               .valueMap()
                                               .with(WithOptions.tokens)
                                               .fold())
                                         .by(__.out(ArticleToSkillEdge.TEACH.getString())
                                               .hasLabel(VertexLabel.SKILL.getString())
                                               .valueMap()
                                               .project("skillVertex", "teachEdge") //TODO: もう少し良い書き方あるので変更する
                                               .by(__.valueMap().with(WithOptions.tokens))
                                               .by(__.inE(ArticleToSkillEdge.TEACH.getString())
                                                     .where((outV().hasId(articleId)).valueMap()))
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
                                         .by(coalesce(__.inE(UserToArticleEdge.LIKED.getString())
                                                        .where(outV().hasId(userId)
                                                                     .hasLabel(VertexLabel.USER.getString()))
                                                        .limit(1)
                                                        .constant(true), constant(false)))
                                         .by(coalesce(__.inE(UserToArticleEdge.LEARNED.getString())
                                                        .where(outV().hasId(userId)
                                                                     .hasLabel(VertexLabel.USER.getString()))
                                                        .limit(1)
                                                        .constant(true), constant(false)))
                                         .by(__.in(UserToArticleEdge.LIKED.getString()).count())
                                         .by(__.in(UserToArticleEdge.LEARNED.getString()).count())
                                         .next();

        return ArticleEntityConverter.toArticleEntity(allResult);
    }

    @Override
    public ArticleEntity findOneForGuest(String articleId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        Map<String, Object> allResult = g.V(articleId)
                                         .project("base", "tags", "skills", "author", "status", "liked", "learned")
                                         .by(__.valueMap().with(WithOptions.tokens))
                                         .by(__.out(ArticleToTagEdge.RELATED.getString())
                                               .hasLabel(VertexLabel.TAG.getString())
                                               .valueMap()
                                               .with(WithOptions.tokens)
                                               .fold())
                                         .by(__.out(ArticleToSkillEdge.TEACH.getString())
                                               .hasLabel(VertexLabel.SKILL.getString())
                                               .valueMap()
                                               .project("skillVertex", "teachEdge") //TODO: もう少し良い書き方あるので変更する
                                               .by(__.valueMap().with(WithOptions.tokens))
                                               .by(__.inE(ArticleToSkillEdge.TEACH.getString())
                                                     .where((outV().hasId(articleId)).valueMap()))
                                               .fold())
                                         .by(__.in(UserToArticleEdge.DRAFTED.getString(),
                                                   UserToArticleEdge.DELETED.getString(),
                                                   UserToArticleEdge.PUBLISHED.getString())
                                               .hasLabel(VertexLabel.USER.getString())
                                               .valueMap()
                                               .with(WithOptions.tokens))
                                         .by(__.inE(UserToArticleEdge.DRAFTED.getString(),
                                                    UserToArticleEdge.DELETED.getString(),
                                                    UserToArticleEdge.PUBLISHED.getString()).label())
                                         .by(__.in(UserToArticleEdge.LIKED.getString()).count())
                                         .by(__.in(UserToArticleEdge.LEARNED.getString()).count())
                                         .next();

        return ArticleEntityConverter.toArticleEntityForGuest(allResult);
    }

    @Override
    public List<ArticleEntity> findCreated(String userId, SearchCondition searchCondition) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        List<Map<String, Object>> allResults;
        if (searchCondition.shouldSort()) {
            Order orderType = searchCondition.isAscend() ? Order.asc : Order.desc;
            if (searchCondition.vertexSort()) {
                allResults = g.V(userId)
                              .out(UserToArticleEdge.PUBLISHED.getString())
                              .hasLabel(VertexLabel.ARTICLE.getString())
                              .order()
                              .by(searchCondition.getField(), orderType)
                              .range(searchCondition.getRangeStart(), searchCondition.getRangeEnd())
                              .project("base", "tags", "skills", "liked", "learned")
                              .by(__.valueMap().with(WithOptions.tokens))
                              .by(__.out(ArticleToTagEdge.RELATED.getString())
                                    .hasLabel(VertexLabel.TAG.getString())
                                    .valueMap()
                                    .with(WithOptions.tokens)
                                    .fold())
                              .by(__.outE(ArticleToSkillEdge.TEACH.getString())
                                    .hasLabel(VertexLabel.SKILL.getString())
                                    .as("teachEdge")
                                    .inV()
                                    .as("skillVertex")
                                    .select("teachEdge", "skillVertex")
                                    .by(valueMap())
                                    .fold())
                              .by(__.in(UserToArticleEdge.LIKED.getString()).count())
                              .by(__.in(UserToArticleEdge.LEARNED.getString()).count())
                              .toList();
            } else {
                allResults = g.V(userId)
                              .out(UserToArticleEdge.PUBLISHED.getString())
                              .hasLabel(VertexLabel.ARTICLE.getString())
                              .project("base", "tags", "skills", "liked", "learned", "sortEdge")
                              .by(__.valueMap().with(WithOptions.tokens))
                              .by(__.out(ArticleToTagEdge.RELATED.getString())
                                    .hasLabel(VertexLabel.TAG.getString())
                                    .valueMap()
                                    .with(WithOptions.tokens)
                                    .fold())
                              .by(__.outE(ArticleToSkillEdge.TEACH.getString())
                                    .hasLabel(VertexLabel.SKILL.getString())
                                    .as("teachEdge")
                                    .inV()
                                    .as("skillVertex")
                                    .select("teachEdge", "skillVertex")
                                    .by(valueMap())
                                    .fold())
                              .by(__.in(UserToArticleEdge.LIKED.getString()).count())
                              .by(__.in(UserToArticleEdge.LEARNED.getString()).count())
                              .by(__.in(searchCondition.getField()).count())
                              .order()
                              .by(select("sortEdge"), orderType)
                              .range(searchCondition.getRangeStart(), searchCondition.getRangeEnd())
                              .toList();
            }
        } else {
            allResults = g.V(userId)
                          .out(UserToArticleEdge.PUBLISHED.getString())
                          .hasLabel(VertexLabel.ARTICLE.getString())
                          .range(searchCondition.getRangeStart(), searchCondition.getRangeEnd())
                          .project("base", "tags", "skills", "liked", "learned")
                          .by(__.valueMap().with(WithOptions.tokens))
                          .by(__.out(ArticleToTagEdge.RELATED.getString())
                                .hasLabel(VertexLabel.TAG.getString())
                                .valueMap()
                                .with(WithOptions.tokens)
                                .fold())
                          .by(__.outE(ArticleToSkillEdge.TEACH.getString())
                                .hasLabel(VertexLabel.SKILL.getString())
                                .as("teachEdge")
                                .inV()
                                .as("skillVertex")
                                .select("teachEdge", "skillVertex")
                                .by(valueMap())
                                .fold())
                          .by(__.in(UserToArticleEdge.LIKED.getString()).count())
                          .by(__.in(UserToArticleEdge.LEARNED.getString()).count())
                          .toList();
        }

        return allResults.stream()
                         .map(r -> ArticleEntityConverter.toArticleEntityForList(r))
                         .collect(Collectors.toList());
    }

    @Override
    public List<ArticleEntity> findCreatedBySelf(String userId, SelfSearchCondition searchCondition) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        List<Map<String, Object>> allResults;
        if (searchCondition.shouldFilter()) {
            allResults = findCreatedBySelfWithFilter(g, userId, searchCondition);
        } else {
            allResults = findCreatedBySelfWithoutFilter(g, userId, searchCondition);
        }

        return allResults.stream()
                         .map(r -> ArticleEntityConverter.toArticleEntityForListBySelf(r))
                         .collect(Collectors.toList());
    }

    private List<Map<String, Object>> findCreatedBySelfWithFilter(
        GraphTraversalSource g, String userId, SelfSearchCondition searchCondition) {

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
                        .by(__.out(ArticleToTagEdge.RELATED.getString())
                              .hasLabel(VertexLabel.TAG.getString())
                              .valueMap()
                              .with(WithOptions.tokens)
                              .fold())
                        .by(__.outE(ArticleToSkillEdge.TEACH.getString())
                              .hasLabel(VertexLabel.SKILL.getString())
                              .as("teachEdge")
                              .inV()
                              .as("skillVertex")
                              .select("teachEdge", "skillVertex")
                              .by(valueMap())
                              .fold())
                        .by(__.inE(searchCondition.getFilter()).label())
                        .by(__.in(UserToArticleEdge.LIKED.getString()).count())
                        .by(__.in(UserToArticleEdge.LEARNED.getString()).count())
                        .toList();
            } else {
                return g.V(userId)
                        .out(searchCondition.getFilter())
                        .hasLabel(VertexLabel.ARTICLE.getString())
                        .project("base", "tags", "skills", "status", "liked", "learned", "sortEdge")
                        .by(__.valueMap().with(WithOptions.tokens))
                        .by(__.out(ArticleToTagEdge.RELATED.getString())
                              .hasLabel(VertexLabel.TAG.getString())
                              .valueMap()
                              .with(WithOptions.tokens)
                              .fold())
                        .by(__.outE(ArticleToSkillEdge.TEACH.getString())
                              .hasLabel(VertexLabel.SKILL.getString())
                              .as("teachEdge")
                              .inV()
                              .as("skillVertex")
                              .select("teachEdge", "skillVertex")
                              .by(valueMap())
                              .fold())
                        .by(__.inE(searchCondition.getFilter()).label())
                        .by(__.in(UserToArticleEdge.LIKED.getString()).count())
                        .by(__.in(UserToArticleEdge.LEARNED.getString()).count())
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
                    .by(__.out(ArticleToTagEdge.RELATED.getString())
                          .hasLabel(VertexLabel.TAG.getString())
                          .valueMap()
                          .with(WithOptions.tokens)
                          .fold())
                    .by(__.outE(ArticleToSkillEdge.TEACH.getString())
                          .hasLabel(VertexLabel.SKILL.getString())
                          .as("teachEdge")
                          .inV()
                          .as("skillVertex")
                          .select("teachEdge", "skillVertex")
                          .by(valueMap())
                          .fold())
                    .by(__.inE(searchCondition.getFilter()).label())
                    .by(__.in(UserToArticleEdge.LIKED.getString()).count())
                    .by(__.in(UserToArticleEdge.LEARNED.getString()).count())
                    .toList();
        }
    }

    private List<Map<String, Object>> findCreatedBySelfWithoutFilter(
        GraphTraversalSource g, String userId, SelfSearchCondition searchCondition) {
        if (searchCondition.shouldSort()) {
            Order orderType = searchCondition.isAscend() ? Order.asc : Order.desc;
            if (searchCondition.vertexSort()) {
                return g.V(userId)
                        .out(UserToContentProperty.DRAFTED.getString(), UserToContentProperty.PUBLISHED.getString())
                        .hasLabel(VertexLabel.ARTICLE.getString())
                        .order()
                        .by(searchCondition.getField(), orderType)
                        .range(searchCondition.getRangeStart(), searchCondition.getRangeEnd())
                        .project("base", "tags", "skills", "status", "liked", "learned")
                        .by(__.valueMap().with(WithOptions.tokens))
                        .by(__.out(ArticleToTagEdge.RELATED.getString())
                              .hasLabel(VertexLabel.TAG.getString())
                              .valueMap()
                              .with(WithOptions.tokens)
                              .fold())
                        .by(__.outE(ArticleToSkillEdge.TEACH.getString())
                              .hasLabel(VertexLabel.SKILL.getString())
                              .as("teachEdge")
                              .inV()
                              .as("skillVertex")
                              .select("teachEdge", "skillVertex")
                              .by(valueMap())
                              .fold())
                        .by(__.inE(UserToContentProperty.DRAFTED.getString(),
                                   UserToContentProperty.PUBLISHED.getString()).label())
                        .by(__.in(UserToArticleEdge.LIKED.getString()).count())
                        .by(__.in(UserToArticleEdge.LEARNED.getString()).count())
                        .toList();
            } else {
                return g.V(userId)
                        .out(UserToContentProperty.DRAFTED.getString(), UserToContentProperty.PUBLISHED.getString())
                        .hasLabel(VertexLabel.ARTICLE.getString())
                        .project("base", "tags", "skills", "status", "liked", "learned", "sortEdge")
                        .by(__.valueMap().with(WithOptions.tokens))
                        .by(__.out(ArticleToTagEdge.RELATED.getString())
                              .hasLabel(VertexLabel.TAG.getString())
                              .valueMap()
                              .with(WithOptions.tokens)
                              .fold())
                        .by(__.outE(ArticleToSkillEdge.TEACH.getString())
                              .hasLabel(VertexLabel.SKILL.getString())
                              .as("teachEdge")
                              .inV()
                              .as("skillVertex")
                              .select("teachEdge", "skillVertex")
                              .by(valueMap())
                              .fold())
                        .by(__.inE(UserToContentProperty.DRAFTED.getString(),
                                   UserToContentProperty.PUBLISHED.getString()).label())
                        .by(__.in(UserToArticleEdge.LIKED.getString()).count())
                        .by(__.in(UserToArticleEdge.LEARNED.getString()).count())
                        .by(__.in(searchCondition.getField()).count())
                        .order()
                        .by(select("sortEdge"), orderType)
                        .range(searchCondition.getRangeStart(), searchCondition.getRangeEnd())
                        .toList();
            }
        } else {
            return g.V(userId)
                    .out(UserToContentProperty.DRAFTED.getString(), UserToContentProperty.PUBLISHED.getString())
                    .hasLabel(VertexLabel.ARTICLE.getString())
                    .range(searchCondition.getRangeStart(), searchCondition.getRangeEnd())
                    .project("base", "tags", "skills", "status", "liked", "learned")
                    .by(__.valueMap().with(WithOptions.tokens))
                    .by(__.out(ArticleToTagEdge.RELATED.getString())
                          .hasLabel(VertexLabel.TAG.getString())
                          .valueMap()
                          .with(WithOptions.tokens)
                          .fold())
                    .by(__.outE(ArticleToSkillEdge.TEACH.getString())
                          .hasLabel(VertexLabel.SKILL.getString())
                          .as("teachEdge")
                          .inV()
                          .as("skillVertex")
                          .select("teachEdge", "skillVertex")
                          .by(valueMap())
                          .fold())
                    .by(__.inE(UserToContentProperty.DRAFTED.getString(), UserToContentProperty.PUBLISHED.getString())
                          .label())
                    .by(__.in(UserToArticleEdge.LIKED.getString()).count())
                    .by(__.in(UserToArticleEdge.LEARNED.getString()).count())
                    .toList();
        }
    }

    @Override
    public List<ArticleEntity> findActioned(String userId, SearchCondition searchCondition) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        List<Map<String, Object>> allResults;
        if (searchCondition.shouldSort()) {
            Order orderType = searchCondition.isAscend() ? Order.asc : Order.desc;
            if (searchCondition.vertexSort()) {
                allResults = g.V(userId)
                              .out(searchCondition.ge)
                              .hasLabel(VertexLabel.ARTICLE.getString())
                              .order()
                              .by(searchCondition.getField(), orderType)
                              .range(searchCondition.getRangeStart(), searchCondition.getRangeEnd())
                              .project("base", "tags", "skills", "liked", "learned")
                              .by(__.valueMap().with(WithOptions.tokens))
                              .by(__.out(ArticleToTagEdge.RELATED.getString())
                                    .hasLabel(VertexLabel.TAG.getString())
                                    .valueMap()
                                    .with(WithOptions.tokens)
                                    .fold())
                              .by(__.outE(ArticleToSkillEdge.TEACH.getString())
                                    .hasLabel(VertexLabel.SKILL.getString())
                                    .as("teachEdge")
                                    .inV()
                                    .as("skillVertex")
                                    .select("teachEdge", "skillVertex")
                                    .by(valueMap())
                                    .fold())
                              .by(__.in(UserToArticleEdge.LIKED.getString()).count())
                              .by(__.in(UserToArticleEdge.LEARNED.getString()).count())
                              .toList();
            } else {
                allResults = g.V(userId)
                              .out(UserToArticleEdge.PUBLISHED.getString())
                              .hasLabel(VertexLabel.ARTICLE.getString())
                              .project("base", "tags", "skills", "liked", "learned", "sortEdge")
                              .by(__.valueMap().with(WithOptions.tokens))
                              .by(__.out(ArticleToTagEdge.RELATED.getString())
                                    .hasLabel(VertexLabel.TAG.getString())
                                    .valueMap()
                                    .with(WithOptions.tokens)
                                    .fold())
                              .by(__.outE(ArticleToSkillEdge.TEACH.getString())
                                    .hasLabel(VertexLabel.SKILL.getString())
                                    .as("teachEdge")
                                    .inV()
                                    .as("skillVertex")
                                    .select("teachEdge", "skillVertex")
                                    .by(valueMap())
                                    .fold())
                              .by(__.in(UserToArticleEdge.LIKED.getString()).count())
                              .by(__.in(UserToArticleEdge.LEARNED.getString()).count())
                              .by(__.in(searchCondition.getField()).count())
                              .order()
                              .by(select("sortEdge"), orderType)
                              .range(searchCondition.getRangeStart(), searchCondition.getRangeEnd())
                              .toList();
            }
        } else {
            allResults = g.V(userId)
                          .out(UserToArticleEdge.PUBLISHED.getString())
                          .hasLabel(VertexLabel.ARTICLE.getString())
                          .range(searchCondition.getRangeStart(), searchCondition.getRangeEnd())
                          .project("base", "tags", "skills", "liked", "learned")
                          .by(__.valueMap().with(WithOptions.tokens))
                          .by(__.out(ArticleToTagEdge.RELATED.getString())
                                .hasLabel(VertexLabel.TAG.getString())
                                .valueMap()
                                .with(WithOptions.tokens)
                                .fold())
                          .by(__.outE(ArticleToSkillEdge.TEACH.getString())
                                .hasLabel(VertexLabel.SKILL.getString())
                                .as("teachEdge")
                                .inV()
                                .as("skillVertex")
                                .select("teachEdge", "skillVertex")
                                .by(valueMap())
                                .fold())
                          .by(__.in(UserToArticleEdge.LIKED.getString()).count())
                          .by(__.in(UserToArticleEdge.LEARNED.getString()).count())
                          .toList();
        }

        return allResults.stream()
                         .map(r -> ArticleEntityConverter.toArticleEntityForList(r))
                         .collect(Collectors.toList());
    }

    @Override
    public List<ArticleDetailEntity> findFamous(String requesterId) {
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
                                                         "userLiked",
                                                         "userLearned",
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
                                                .by(coalesce(__.inE(UserToArticleEdge.LIKED.getString())
                                                               .where(outV().hasId(requesterId)
                                                                            .hasLabel(VertexLabel.USER.getString()))
                                                               .limit(1)
                                                               .constant(true), constant(false)))
                                                .by(coalesce(__.inE(UserToArticleEdge.LEARNED.getString())
                                                               .where(outV().hasId(requesterId)
                                                                            .hasLabel(VertexLabel.USER.getString()))
                                                               .limit(1)
                                                               .constant(true), constant(false)))
                                                .by(__.in(UserToArticleEdge.LIKED.getString()).count())
                                                .by(__.in(UserToArticleEdge.LEARNED.getString()).count())
                                                .toList();

        return allResults.stream()
                         .map(r -> ArticleDetailEntityConverter.toArticleDetailEntity(r))
                         .collect(Collectors.toList());
    }

    @Override
    public List<ArticleDetailEntity> findFamousForGuest() {
        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<String, Object>> allResults = g.V()
                                                .hasLabel(VertexLabel.ARTICLE.getString())
                                                .filter(inE().hasLabel(UserToArticleEdge.PUBLISHED.getString()))
                                                .order()
                                                .by(values(ArticleVertexProperty.LIKED.getString(),
                                                           ArticleVertexProperty.LEARNED.getString()).sum(), Order.desc)
                                                .limit(10)
                                                .project("base", "tags", "author", "status", "liked", "learned")
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
                                                .by(__.in(UserToArticleEdge.LIKED.getString()).count())
                                                .by(__.in(UserToArticleEdge.LEARNED.getString()).count())
                                                .toList();

        return allResults.stream()
                         .map(r -> ArticleDetailEntityConverter.toArticleDetailEntityForGuest(r))
                         .collect(Collectors.toList());
    }

    public List<ArticleDetailEntity> findRelated(String userId) {
        return null;
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
}
