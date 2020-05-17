package jp.boosty.backend.infrastructure.repository.page;

import jp.boosty.backend.domain.GraphQLCustomException;
import jp.boosty.backend.infrastructure.constant.GraphQLErrorMessage;
import jp.boosty.backend.infrastructure.constant.edge.EdgeLabel;
import jp.boosty.backend.infrastructure.constant.vertex.property.DateProperty;
import jp.boosty.backend.infrastructure.constant.vertex.property.PageVertexProperty;
import jp.boosty.backend.infrastructure.converter.entity.page.LikedPageEntityConverter;
import jp.boosty.backend.infrastructure.datamodel.page.LikedPageListEntity;

import org.apache.tinkerpop.gremlin.process.traversal.Order;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.WithOptions;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jp.boosty.backend.domain.domainmodel.search.SearchCondition;
import jp.boosty.backend.domain.repository.page.PageQueryRepository;
import jp.boosty.backend.infrastructure.connector.NeptuneClient;
import jp.boosty.backend.infrastructure.constant.edge.property.ViewEdgeProperty;
import jp.boosty.backend.infrastructure.constant.vertex.label.VertexLabel;
import jp.boosty.backend.infrastructure.converter.entity.page.PageEntityConverter;
import jp.boosty.backend.infrastructure.datamodel.page.PageEntity;
import jp.boosty.backend.infrastructure.datamodel.page.PageListEntity;

import lombok.extern.slf4j.Slf4j;

import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.coalesce;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.constant;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.inE;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.inV;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.outV;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.values;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.select;

@Slf4j
@Component
public class PageQueryRepositoryImpl implements PageQueryRepository {

    private final NeptuneClient neptuneClient;

    public PageQueryRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    @Override
    public PageEntity findOne(String bookId, String pageId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        Map<String, Object> result;
        try {
            result = g.V(pageId)
                      .project("base", "liked", "likeCount")
                      .by(__.valueMap().with(WithOptions.tokens))
                      .by(__.coalesce(__.inE(EdgeLabel.LIKE.getString())
                                        .where(outV().hasId(userId).hasLabel(VertexLabel.USER.getString()))
                                        .limit(1)
                                        .constant(true), constant(false)))
                      .by(__.in(EdgeLabel.LIKE.getString()).count())
                      .next();

            g.V(userId)
             .hasLabel(VertexLabel.USER.getString())
             .outE(EdgeLabel.VIEW.getString())
             .where(inV().hasId(bookId).hasLabel(VertexLabel.BOOK.getString()))
             .drop()
             .iterate();

            g.V(userId)
             .hasLabel(VertexLabel.USER.getString())
             .addE(EdgeLabel.VIEW.getString())
             .property(DateProperty.CREATE_TIME.getString(), now)
             .property(ViewEdgeProperty.PAGE_ID.getString(), pageId)
             .to(g.V(bookId).hasLabel(VertexLabel.BOOK.getString()))
             .next();
        } catch (Exception e) {
            log.error("findOne error: {} {} {} {}", bookId, userId, pageId, e.getMessage()); throw new GraphQLCustomException(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
        return PageEntityConverter.toPageEntity(result);
    }

    @Override
    public PageEntity findOneForGuest(String pageId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        Map<String, Object> result;
        try {
            result = g.V(pageId)
                      .project("base", "likeCount")
                      .by(__.valueMap().with(WithOptions.tokens))
                      .by(__.in(EdgeLabel.LIKE.getString()).count())
                      .next();

        } catch (Exception e) {
            log.error("findOneForGuest error: {} {}", pageId, e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
        return PageEntityConverter.toPageEntityForGuest(result);
    }

    @Override
    public PageEntity findOneToEdit(String pageId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        Map<String, Object> result;
        try {
            result = g.V(pageId)
                      .project("base", "liked", "likeCount")
                      .by(__.valueMap().with(WithOptions.tokens))
                      .by(__.coalesce(__.inE(EdgeLabel.LIKE.getString())
                                        .where(outV().hasId(userId).hasLabel(VertexLabel.USER.getString()))
                                        .limit(1)
                                        .constant(true), constant(false)))
                      .by(__.in(EdgeLabel.LIKE.getString()).count())
                      .next();

        } catch (Exception e) {
            log.error("findOneToEdit error: {} {} {}", pageId, userId, e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
        return PageEntityConverter.toPageEntity(result);
    }

    @Override
    public PageListEntity findCreated(String userId, SearchCondition searchCondition) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        List<Map<String, Object>> allResults;
        long sumCount = findPublishedSumCount(g, userId);
        try {
            if (searchCondition.shouldSort()) {
                Order orderType = searchCondition.isAscend() ? Order.asc : Order.desc;
                if (searchCondition.vertexSort()) {
                    allResults = g.V(userId)
                                  .out(EdgeLabel.PUBLISH.getString())
                                  .hasLabel(VertexLabel.PAGE.getString())
                                  .order()
                                  .by(searchCondition.getField(), orderType)
                                  .range(searchCondition.getRangeStart(), searchCondition.getRangeEnd())
                                  .project("base", "liked", "likeCount")
                                  .by(__.valueMap().with(WithOptions.tokens))
                                  .by(__.coalesce(__.inE(EdgeLabel.LIKE.getString())
                                                    .where(outV().hasId(userId).hasLabel(VertexLabel.USER.getString()))
                                                    .limit(1)
                                                    .constant(true), constant(false)))
                                  .by(__.in(EdgeLabel.LIKE.getString()).count())
                                  .toList();
                } else {
                    allResults = g.V(userId)
                                  .out(EdgeLabel.PUBLISH.getString())
                                  .hasLabel(VertexLabel.PAGE.getString())
                                  .project("base", "liked", "likeCount", "sortEdge")
                                  .by(__.valueMap().with(WithOptions.tokens))
                                  .by(__.coalesce(__.inE(EdgeLabel.LIKE.getString())
                                                    .where(outV().hasId(userId).hasLabel(VertexLabel.USER.getString()))
                                                    .limit(1)
                                                    .constant(true), constant(false)))
                                  .by(__.in(EdgeLabel.LIKE.getString()).count())
                                  .by(__.in(searchCondition.getField()).count())
                                  .order()
                                  .by(select("sortEdge"), orderType)
                                  .range(searchCondition.getRangeStart(), searchCondition.getRangeEnd())
                                  .toList();
                }
            } else {
                allResults = g.V(userId)
                              .out(EdgeLabel.PUBLISH.getString())
                              .hasLabel(VertexLabel.PAGE.getString())
                              .range(searchCondition.getRangeStart(), searchCondition.getRangeEnd())
                              .project("base", "liked", "likeCount")
                              .by(__.valueMap().with(WithOptions.tokens))
                              .by(__.coalesce(__.inE(EdgeLabel.LIKE.getString())
                                                .where(outV().hasId(userId).hasLabel(VertexLabel.USER.getString()))
                                                .limit(1)
                                                .constant(true), constant(false)))
                              .by(__.in(EdgeLabel.LIKE.getString()).count())
                              .toList();
            }
        } catch (Exception e) {
            log.error("findCreated error: {} {} {}", userId, searchCondition, e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }

        return PageListEntity.builder()
                             .pages(allResults.stream()
                                              .map(r -> PageEntityConverter.toPageEntity(r))
                                              .collect(Collectors.toList()))
                             .sumCount(sumCount)
                             .build();
    }

    @Override
    public PageListEntity findCreatedBySelf(String userId, SearchCondition searchCondition) {
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

        return PageListEntity.builder()
                             .pages(allResults.stream()
                                              .map(r -> PageEntityConverter.toPageEntity(r))
                                              .collect(Collectors.toList()))
                             .sumCount(sumCount)
                             .build();
    }

    @Override
    public LikedPageListEntity findLiked(String userId, int page) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        List<Map<String, Object>> allResults;
        long sumCount;

        try {
            allResults = g.V(userId)
                          .hasLabel(VertexLabel.USER.getString())
                          .out(EdgeLabel.LIKE.getString())
                          .hasLabel(VertexLabel.PAGE.getString())
                          .order()
                          .by(coalesce(inE(EdgeLabel.LIKE.getString()).values(DateProperty.CREATE_TIME.getString()),
                                       values(DateProperty.CREATE_TIME.getString())), Order.desc)
                          .range(24 * (page - 1), 24 * page)
                          .project("base", "book")
                          .by(__.valueMap().with(WithOptions.tokens))
                          .by(__.in(EdgeLabel.INCLUDE.getString())
                                .in(EdgeLabel.INCLUDE.getString())
                                .hasLabel(VertexLabel.BOOK.getString())
                                .valueMap()
                                .with(WithOptions.tokens))
                          .toList();

            sumCount = g.V(userId)
                        .hasLabel(VertexLabel.USER.getString())
                        .out(EdgeLabel.LIKE.getString())
                        .hasLabel(VertexLabel.PAGE.getString())
                        .count()
                        .next();

        } catch (Exception e) {
            log.error("findLiked error: {} {} {}", userId, page, e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
        return LikedPageListEntity.builder()
                                  .likedPages(allResults.stream()
                                                        .map(r -> LikedPageEntityConverter.toLikedPageEntity((r)))
                                                        .collect(Collectors.toList()))
                                  .sumCount(sumCount)
                                  .build();
    }

    private long findSumCountWithFilter(GraphTraversalSource g, String userId, SearchCondition searchCondition) {
        try {
            return g.V(userId).out(searchCondition.getFilter()).hasLabel(VertexLabel.PAGE.getString()).count().next();
        } catch (Exception e) {
            log.error("findSumCountWithFilter error: {} {} {}", userId, searchCondition, e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }

    private long findSumCountWithoutFilter(GraphTraversalSource g, String userId) {
        try {
            return g.V(userId)
                    .out(EdgeLabel.DRAFT.getString(), EdgeLabel.PUBLISH.getString())
                    .hasLabel(VertexLabel.PAGE.getString())
                    .count()
                    .next();
        } catch (Exception e) {
            log.error("findSumCountWithoutFilter error: {} {}", userId, e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }

    private long findPublishedSumCount(GraphTraversalSource g, String userId) {
        try {
            return g.V(userId).out(EdgeLabel.PUBLISH.getString()).hasLabel(VertexLabel.PAGE.getString()).count().next();
        } catch (Exception e) {
            log.error("findPublishedSumCount error: {} {}", userId, e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }

    private List<Map<String, Object>> findCreatedBySelfWithFilter(
        GraphTraversalSource g, String userId, SearchCondition searchCondition) {

        try {
            if (searchCondition.shouldSort()) {
                Order orderType = searchCondition.isAscend() ? Order.asc : Order.desc;
                if (searchCondition.vertexSort()) {
                    return g.V(userId)
                            .out(searchCondition.getFilter())
                            .hasLabel(VertexLabel.PAGE.getString())
                            .order()
                            .by(searchCondition.getField(), orderType)
                            .range(searchCondition.getRangeStart(), searchCondition.getRangeEnd())
                            .project("base", "liked", "likeCount")
                            .by(__.valueMap().with(WithOptions.tokens))
                            .by(__.coalesce(__.inE(EdgeLabel.LIKE.getString())
                                              .where(outV().hasId(userId).hasLabel(VertexLabel.USER.getString()))
                                              .limit(1)
                                              .constant(true), constant(false)))
                            .by(__.in(EdgeLabel.LIKE.getString()).count())
                            .toList();
                } else {
                    return g.V(userId)
                            .out(searchCondition.getFilter())
                            .hasLabel(VertexLabel.PAGE.getString())
                            .project("base", "liked", "likeCount", "sortEdge")
                            .by(__.valueMap().with(WithOptions.tokens))
                            .by(__.coalesce(__.inE(EdgeLabel.LIKE.getString())
                                              .where(outV().hasId(userId).hasLabel(VertexLabel.USER.getString()))
                                              .limit(1)
                                              .constant(true), constant(false)))
                            .by(__.in(EdgeLabel.LIKE.getString()).count())
                            .by(__.in(searchCondition.getField()).count())
                            .order()
                            .by(select("sortEdge"), orderType)
                            .range(searchCondition.getRangeStart(), searchCondition.getRangeEnd())
                            .toList();
                }
            } else {
                return g.V(userId)
                        .out(searchCondition.getFilter())
                        .hasLabel(VertexLabel.PAGE.getString())
                        .range(searchCondition.getRangeStart(), searchCondition.getRangeEnd())
                        .project("base", "liked", "likeCount")
                        .by(__.valueMap().with(WithOptions.tokens))
                        .by(__.coalesce(__.inE(EdgeLabel.LIKE.getString())
                                          .where(outV().hasId(userId).hasLabel(VertexLabel.USER.getString()))
                                          .limit(1)
                                          .constant(true), constant(false)))
                        .by(__.in(EdgeLabel.LIKE.getString()).count())
                        .toList();
            }
        } catch (Exception e) {
            log.error("findCreatedBySelfWithFilter error: {} {} {}", userId, searchCondition, e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }

    private List<Map<String, Object>> findCreatedBySelfWithoutFilter(
        GraphTraversalSource g, String userId, SearchCondition searchCondition) {
        try {
            if (searchCondition.shouldSort()) {
                Order orderType = searchCondition.isAscend() ? Order.asc : Order.desc;
                if (searchCondition.vertexSort()) {
                    return g.V(userId)
                            .out(EdgeLabel.DRAFT.getString(), EdgeLabel.PUBLISH.getString())
                            .hasLabel(VertexLabel.PAGE.getString())
                            .order()
                            .by(searchCondition.getField(), orderType)
                            .range(searchCondition.getRangeStart(), searchCondition.getRangeEnd())
                            .project("base", "liked", "likeCount")
                            .by(__.valueMap().with(WithOptions.tokens))
                            .by(__.coalesce(__.inE(EdgeLabel.LIKE.getString())
                                              .where(outV().hasId(userId).hasLabel(VertexLabel.USER.getString()))
                                              .limit(1)
                                              .constant(true), constant(false)))
                            .by(__.in(EdgeLabel.LIKE.getString()).count())
                            .toList();
                } else {
                    return g.V(userId)
                            .out(EdgeLabel.DRAFT.getString(), EdgeLabel.PUBLISH.getString())
                            .hasLabel(VertexLabel.PAGE.getString())
                            .project("base", "liked", "likeCount", "sortEdge")
                            .by(__.valueMap().with(WithOptions.tokens))
                            .by(__.coalesce(__.inE(EdgeLabel.LIKE.getString())
                                              .where(outV().hasId(userId).hasLabel(VertexLabel.USER.getString()))
                                              .limit(1)
                                              .constant(true), constant(false)))
                            .by(__.in(EdgeLabel.LIKE.getString()).count())
                            .by(__.in(searchCondition.getField()).count())
                            .order()
                            .by(select("sortEdge"), orderType)
                            .range(searchCondition.getRangeStart(), searchCondition.getRangeEnd())
                            .toList();
                }
            } else {
                return g.V(userId)
                        .out(EdgeLabel.DRAFT.getString(), EdgeLabel.PUBLISH.getString())
                        .hasLabel(VertexLabel.PAGE.getString())
                        .range(searchCondition.getRangeStart(), searchCondition.getRangeEnd())
                        .project("base", "liked", "likeCount")
                        .by(__.valueMap().with(WithOptions.tokens))
                        .by(__.coalesce(__.inE(EdgeLabel.LIKE.getString())
                                          .where(outV().hasId(userId).hasLabel(VertexLabel.USER.getString()))
                                          .limit(1)
                                          .constant(true), constant(false)))
                        .by(__.in(EdgeLabel.LIKE.getString()).count())
                        .toList();
            }
        } catch (Exception e) {
            log.error("findCreatedBySelfWithoutFilter error: {} {} {}", userId, searchCondition, e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }

    @Override
    public String findAuthorId(String pageId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        try {
            return g.V(pageId)
                    .hasLabel(VertexLabel.PAGE.getString())
                    .in(EdgeLabel.PUBLISH.getString())
                    .hasLabel(VertexLabel.USER.getString())
                    .id()
                    .next()
                    .toString();
        } catch (Exception e) {
            log.error("findAuthorId error: {} {}", pageId, e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }

    @Override
    public boolean canPreview(String pageId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        try {
            return (boolean) g.V(pageId)
                              .hasLabel(VertexLabel.PAGE.getString())
                              .values(PageVertexProperty.CAN_PREVIEW.getString())
                              .next();
        } catch (Exception e) {
            log.error("canPreview error: {} {}", pageId, e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }
}
