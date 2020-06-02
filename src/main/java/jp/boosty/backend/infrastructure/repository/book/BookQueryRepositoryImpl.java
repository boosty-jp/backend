package jp.boosty.backend.infrastructure.repository.book;

import jp.boosty.backend.domain.GraphQLCustomException;
import jp.boosty.backend.domain.domainmodel.search.SearchCondition;
import jp.boosty.backend.domain.repository.book.BookQueryRepository;
import jp.boosty.backend.infrastructure.connector.NeptuneClient;
import jp.boosty.backend.infrastructure.constant.GraphQLErrorMessage;
import jp.boosty.backend.infrastructure.constant.edge.EdgeLabel;
import jp.boosty.backend.infrastructure.constant.edge.property.IncludeEdgeProperty;
import jp.boosty.backend.infrastructure.constant.edge.property.ViewEdgeProperty;
import jp.boosty.backend.infrastructure.constant.vertex.label.VertexLabel;
import jp.boosty.backend.infrastructure.constant.vertex.property.BookVertexProperty;
import jp.boosty.backend.infrastructure.constant.vertex.property.DateProperty;
import jp.boosty.backend.infrastructure.constant.vertex.property.PageVertexProperty;
import jp.boosty.backend.infrastructure.converter.entity.book.BookEntityConverter;
import jp.boosty.backend.infrastructure.datamodel.book.BookEntity;
import jp.boosty.backend.infrastructure.datamodel.book.BookListEntity;

import org.apache.tinkerpop.gremlin.process.traversal.Order;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.WithOptions;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.coalesce;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.constant;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.inE;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.outV;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.values;
import static org.apache.tinkerpop.gremlin.process.traversal.TextP.containing;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.has;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.select;

@Component
@Slf4j
public class BookQueryRepositoryImpl implements BookQueryRepository {

    private final NeptuneClient neptuneClient;

    public BookQueryRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    @Override
    public BookEntity findOne(String courseId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        Map<String, Object> allResult;
        try {
            allResult = g.V(courseId)
                         .hasLabel(VertexLabel.BOOK.getString())
                         .project("base",
                                  "features",
                                  "targetDescriptions",
                                  "sections",
                                  "tags",
                                  "author",
                                  "status",
                                  "purchased",
                                  "lastViewedPageId")
                         .by(__.valueMap().with(WithOptions.tokens))
                         .by(__.out(EdgeLabel.INCLUDE.getString())
                               .hasLabel(VertexLabel.BOOK_FEATURE.getString())
                               .valueMap()
                               .fold())
                         .by(__.out(EdgeLabel.INCLUDE.getString())
                               .hasLabel(VertexLabel.BOOK_TARGET_DESCRIPTION.getString())
                               .valueMap()
                               .fold())
                         .by(__.out(EdgeLabel.INCLUDE.getString())
                               .hasLabel(VertexLabel.SECTION.getString())
                               .project("sectionBase", "sectionNumber", "sectionContents")
                               .by(__.valueMap().with(WithOptions.tokens))
                               .by(__.inE(EdgeLabel.INCLUDE.getString()).values(IncludeEdgeProperty.NUMBER.getString()))
                               .by(__.out(EdgeLabel.INCLUDE.getString())
                                     .hasLabel(VertexLabel.PAGE.getString())
                                     .project("pageBase", "pageNumber")
                                     .by(__.valueMap(PageVertexProperty.TITLE.getString(),PageVertexProperty.CAN_PREVIEW.getString()).with(WithOptions.tokens))
                                     .by(__.inE(EdgeLabel.INCLUDE.getString())
                                           .values(IncludeEdgeProperty.NUMBER.getString()))
                                     .fold())
                               .fold())
                         .by(__.out(EdgeLabel.RELATED_TO.getString())
                               .hasLabel(VertexLabel.TAG.getString())
                               .valueMap()
                               .with(WithOptions.tokens)
                               .fold())
                         .by(__.in(EdgeLabel.DRAFT.getString(),
                                   EdgeLabel.DELETE.getString(),
                                   EdgeLabel.SUSPEND.getString(),
                                   EdgeLabel.PUBLISH.getString())
                               .hasLabel(VertexLabel.USER.getString())
                               .valueMap()
                               .with(WithOptions.tokens))
                         .by(__.inE(EdgeLabel.DRAFT.getString(),
                                    EdgeLabel.DELETE.getString(),
                                    EdgeLabel.SUSPEND.getString(),
                                    EdgeLabel.PUBLISH.getString()).label())
                         .by(__.coalesce(__.inE(EdgeLabel.PURCHASE.getString())
                                           .where(outV().hasId(userId).hasLabel(VertexLabel.USER.getString()))
                                           .limit(1)
                                           .constant(true), constant(false)))
                         .by(__.coalesce(__.inE(EdgeLabel.VIEW.getString())
                                           .where(outV().hasId(userId).hasLabel(VertexLabel.USER.getString()))
                                           .values(ViewEdgeProperty.PAGE_ID.getString()), constant("")))
                         .next();
        } catch (Exception e) {
            log.error("find book error: {}", e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }

        return BookEntityConverter.toBookEntity(allResult);
    }

    @Override
    public BookEntity findOneForGuest(String bookId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        Map<String, Object> allResult;
        try {
            allResult = g.V(bookId)
                         .hasLabel(VertexLabel.BOOK.getString())
                         .project("base", "features", "targetDescriptions", "sections", "tags", "author", "status")
                         .by(__.valueMap().with(WithOptions.tokens))
                         .by(__.out(EdgeLabel.INCLUDE.getString())
                               .hasLabel(VertexLabel.BOOK_FEATURE.getString())
                               .valueMap()
                               .fold())
                         .by(__.out(EdgeLabel.INCLUDE.getString())
                               .hasLabel(VertexLabel.BOOK_TARGET_DESCRIPTION.getString())
                               .valueMap()
                               .fold())
                         .by(__.out(EdgeLabel.INCLUDE.getString())
                               .hasLabel(VertexLabel.SECTION.getString())
                               .project("sectionBase", "sectionNumber", "sectionContents")
                               .by(__.valueMap().with(WithOptions.tokens))
                               .by(__.inE(EdgeLabel.INCLUDE.getString()).values(IncludeEdgeProperty.NUMBER.getString()))
                               .by(__.out(EdgeLabel.INCLUDE.getString())
                                     .hasLabel(VertexLabel.PAGE.getString())
                                     .project("pageBase", "pageNumber")
                                     .by(__.valueMap(PageVertexProperty.TITLE.getString(),PageVertexProperty.CAN_PREVIEW.getString()).with(WithOptions.tokens))
                                     .by(__.inE(EdgeLabel.INCLUDE.getString())
                                           .values(IncludeEdgeProperty.NUMBER.getString()))
                                     .fold())
                               .fold())
                         .by(__.out(EdgeLabel.RELATED_TO.getString())
                               .hasLabel(VertexLabel.TAG.getString())
                               .valueMap()
                               .with(WithOptions.tokens)
                               .fold())
                         .by(__.in(EdgeLabel.DRAFT.getString(),
                                   EdgeLabel.DELETE.getString(),
                                   EdgeLabel.SUSPEND.getString(),
                                   EdgeLabel.PUBLISH.getString())
                               .hasLabel(VertexLabel.USER.getString())
                               .valueMap()
                               .with(WithOptions.tokens))
                         .by(__.inE(EdgeLabel.DRAFT.getString(),
                                    EdgeLabel.DELETE.getString(),
                                    EdgeLabel.SUSPEND.getString(),
                                    EdgeLabel.PUBLISH.getString()).label())
                         .next();
        } catch (Exception e) {
            log.error("find book for guest error: {}", e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }


        return BookEntityConverter.toBookEntityForGuest(allResult);
    }

    @Override
    public BookListEntity findCreated(String userId, SearchCondition searchCondition) {
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

        return BookListEntity.builder()
                             .books(allResults.stream()
                                              .map(r -> BookEntityConverter.toBookEntityForListHidePurchaseCount(r))
                                              .collect(Collectors.toList()))
                             .sumCount(sumCount)
                             .build();
    }

    @Override
    public BookListEntity findCreatedBySelf(String userId, SearchCondition searchCondition) {
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

        return BookListEntity.builder()
                             .books(allResults.stream()
                                              .map(r -> BookEntityConverter.toBookEntityForListWithStatus(r))
                                              .collect(Collectors.toList()))
                             .sumCount(sumCount)
                             .build();
    }

    private long findSumCountWithFilter(GraphTraversalSource g, String userId, SearchCondition searchCondition) {
        try {
            return g.V(userId).out(searchCondition.getFilter()).hasLabel(VertexLabel.BOOK.getString()).count().next();
        } catch (Exception e) {
            log.error("findSumCountWithFilter error: {} {} {}", userId, searchCondition, e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }

    private long findSumCountWithoutFilter(GraphTraversalSource g, String userId) {
        try {
            return g.V(userId)
                    .out(EdgeLabel.DRAFT.getString(), EdgeLabel.PUBLISH.getString(), EdgeLabel.SUSPEND.getString())
                    .hasLabel(VertexLabel.BOOK.getString())
                    .count()
                    .next();
        } catch (Exception e) {
            log.error("findSumCountWithoutFilter error: {} {}", userId, e.getMessage());
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
                            .hasLabel(VertexLabel.BOOK.getString())
                            .order()
                            .by(searchCondition.getField(), orderType)
                            .range(searchCondition.getRangeStart(), searchCondition.getRangeEnd())
                            .project("base", "status", "purchaseCount")
                            .by(__.valueMap().with(WithOptions.tokens))
                            .by(__.inE(searchCondition.getFilter()).label())
                            .by(__.in(EdgeLabel.PURCHASE.getString()).count())
                            .toList();
                } else {
                    return g.V(userId)
                            .out(searchCondition.getFilter())
                            .hasLabel(VertexLabel.BOOK.getString())
                            .project("base", "status", "purchaseCount", "sortEdge")
                            .by(__.valueMap().with(WithOptions.tokens))
                            .by(__.inE(searchCondition.getFilter()).label())
                            .by(__.in(EdgeLabel.PURCHASE.getString()).count())
                            .by(__.in(searchCondition.getField()).count())
                            .order()
                            .by(select("sortEdge"), orderType)
                            .range(searchCondition.getRangeStart(), searchCondition.getRangeEnd())
                            .toList();
                }
            } else {
                return g.V(userId)
                        .out(searchCondition.getFilter())
                        .hasLabel(VertexLabel.BOOK.getString())
                        .range(searchCondition.getRangeStart(), searchCondition.getRangeEnd())
                        .project("base", "status", "purchaseCount")
                        .by(__.valueMap().with(WithOptions.tokens))
                        .by(__.inE(searchCondition.getFilter()).label())
                        .by(__.in(EdgeLabel.PURCHASE.getString()).count())
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
                            .out(EdgeLabel.DRAFT.getString(),
                                 EdgeLabel.PUBLISH.getString(),
                                 EdgeLabel.SUSPEND.getString())
                            .hasLabel(VertexLabel.BOOK.getString())
                            .order()
                            .by(searchCondition.getField(), orderType)
                            .range(searchCondition.getRangeStart(), searchCondition.getRangeEnd())
                            .project("base", "status", "purchaseCount")
                            .by(__.valueMap().with(WithOptions.tokens))
                            .by(__.inE(EdgeLabel.DRAFT.getString(),
                                       EdgeLabel.PUBLISH.getString(),
                                       EdgeLabel.SUSPEND.getString()).label())
                            .by(__.in(EdgeLabel.PURCHASE.getString()).count())
                            .toList();
                } else {
                    return g.V(userId)
                            .out(EdgeLabel.DRAFT.getString(),
                                 EdgeLabel.PUBLISH.getString(),
                                 EdgeLabel.SUSPEND.getString())
                            .hasLabel(VertexLabel.BOOK.getString())
                            .project("base", "status", "purchaseCount", "sortEdge")
                            .by(__.valueMap().with(WithOptions.tokens))
                            .by(__.inE(EdgeLabel.DRAFT.getString(),
                                       EdgeLabel.PUBLISH.getString(),
                                       EdgeLabel.SUSPEND.getString()).label())
                            .by(__.in(EdgeLabel.PURCHASE.getString()).count())
                            .by(__.in(searchCondition.getField()).count())
                            .order()
                            .by(select("sortEdge"), orderType)
                            .range(searchCondition.getRangeStart(), searchCondition.getRangeEnd())
                            .toList();
                }
            } else {
                return g.V(userId)
                        .out(EdgeLabel.DRAFT.getString(), EdgeLabel.PUBLISH.getString(), EdgeLabel.SUSPEND.getString())
                        .hasLabel(VertexLabel.BOOK.getString())
                        .range(searchCondition.getRangeStart(), searchCondition.getRangeEnd())
                        .project("base", "status", "purchaseCount")
                        .by(__.valueMap().with(WithOptions.tokens))
                        .by(__.inE(EdgeLabel.DRAFT.getString(),
                                   EdgeLabel.PUBLISH.getString(),
                                   EdgeLabel.SUSPEND.getString()).label())
                        .by(__.in(EdgeLabel.PURCHASE.getString()).count())
                        .toList();
            }
        } catch (Exception e) {
            log.error("findCreatedBySelfWithoutFilter error: {} {} {}", userId, searchCondition, e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }

    @Override
    public List<BookEntity> findActioned(String userId, SearchCondition searchCondition) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        List<Map<String, Object>> allResults;
        try {
            if (searchCondition.shouldSort()) {
                Order orderType = searchCondition.isAscend() ? Order.asc : Order.desc;
                if (searchCondition.vertexSort()) {
                    allResults = g.V(userId)
                                  .out(searchCondition.getFilter())
                                  .hasLabel(VertexLabel.BOOK.getString())
                                  .order()
                                  .by(searchCondition.getField(), orderType)
                                  .range(searchCondition.getRangeStart(), searchCondition.getRangeEnd())
                                  .project("base", "tags", "status", "liked", "learned")
                                  .by(__.valueMap().with(WithOptions.tokens))
                                  .by(__.out(EdgeLabel.RELATED_TO.getString())
                                        .hasLabel(VertexLabel.TAG.getString())
                                        .valueMap()
                                        .with(WithOptions.tokens)
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
                                  .hasLabel(VertexLabel.PAGE.getString())
                                  .project("base", "tags", "status", "liked", "learned", "sortEdge")
                                  .by(__.valueMap().with(WithOptions.tokens))
                                  .by(__.out(EdgeLabel.RELATED_TO.getString())
                                        .hasLabel(VertexLabel.TAG.getString())
                                        .valueMap()
                                        .with(WithOptions.tokens)
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
                              .hasLabel(VertexLabel.PAGE.getString())
                              .range(searchCondition.getRangeStart(), searchCondition.getRangeEnd())
                              .project("base", "tags", "status", "liked", "learned")
                              .by(__.valueMap().with(WithOptions.tokens))
                              .by(__.out(EdgeLabel.RELATED_TO.getString())
                                    .hasLabel(VertexLabel.TAG.getString())
                                    .valueMap()
                                    .with(WithOptions.tokens)
                                    .fold())
                              .by(__.inE(EdgeLabel.DRAFT.getString(),
                                         EdgeLabel.PUBLISH.getString(),
                                         EdgeLabel.DELETE.getString()).label())
                              .by(__.in(EdgeLabel.LIKE.getString()).count())
                              .by(__.in(EdgeLabel.LEARN.getString()).count())
                              .toList();
            }
        } catch (Exception e) {
            log.error("findActioned error: {} {} {}", userId, searchCondition, e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }

        return allResults.stream()
                         .map(r -> BookEntityConverter.toBookEntityForListWithStatus(r))
                         .collect(Collectors.toList());
    }

    @Override
    public BookListEntity findOwn(String userId, int page) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<String, Object>> allResults;
        long sumCount;
        try {
            allResults = g.V(userId)
                          .out(EdgeLabel.PURCHASE.getString())
                          .hasLabel(VertexLabel.BOOK.getString())
                          .order()
                          .by(DateProperty.CREATE_TIME.getString(), Order.desc)
                          .range(24 * (page - 1), 24 * page) //TODO: ドメインロジックに閉じ込める
                          .project("base", "author")
                          .by(__.valueMap().with(WithOptions.tokens))
                          .by(__.in(EdgeLabel.PUBLISH.getString(),
                                    EdgeLabel.DRAFT.getString(),
                                    EdgeLabel.SUSPEND.getString(),
                                    EdgeLabel.DELETE.getString())
                                .hasLabel(VertexLabel.USER.getString())
                                .valueMap()
                                .with(WithOptions.tokens))
                          .toList();

            sumCount =
                g.V(userId).out(EdgeLabel.PURCHASE.getString()).hasLabel(VertexLabel.BOOK.getString()).count().next();
        } catch (Exception e) {
            log.error("findOwn error: {} {} {}", userId, page, e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }

        List<BookEntity> bookEntities =
            allResults.stream().map(r -> BookEntityConverter.toBookEntityForOwnList(r)).collect(Collectors.toList());

        return BookListEntity.builder().books(bookEntities).sumCount(sumCount).build();
    }

    @Override
    public BookListEntity findSearched(String query, int page) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<String, Object>> allResults;
        long sumCount;
        try {
            allResults = g.V()
                          .hasLabel(VertexLabel.BOOK.getString())
                          .or(has(BookVertexProperty.TITLE.getString(), containing(query.toLowerCase())),
                              has(BookVertexProperty.TITLE.getString(), containing(query)),
                              has(BookVertexProperty.TITLE.getString(),
                                  containing(query.substring(0, 1).toUpperCase())),
                              has(BookVertexProperty.TITLE.getString(), containing(query.toUpperCase())))
                          .where(inE().hasLabel(EdgeLabel.PUBLISH.getString()))
                          .project("base", "purchaseCount", "author")
                          .by(__.valueMap().with(WithOptions.tokens))
                          .by(__.in(EdgeLabel.PURCHASE.getString()).count())
                          .by(__.in(EdgeLabel.PUBLISH.getString(),
                                    EdgeLabel.DRAFT.getString(),
                                    EdgeLabel.SUSPEND.getString(),
                                    EdgeLabel.DELETE.getString())
                                .hasLabel(VertexLabel.USER.getString())
                                .valueMap()
                                .with(WithOptions.tokens))
                          .order()
                          .by(select("purchaseCount"), Order.desc)
                          .range(24 * (page - 1), 24 * page)
                          .toList();

            sumCount = g.V()
                        .hasLabel(VertexLabel.BOOK.getString())
                        .has(BookVertexProperty.TITLE.getString(), containing(query))
                        .where(inE().hasLabel(EdgeLabel.PUBLISH.getString()))
                        .count()
                        .next();
        } catch (Exception e) {
            log.error("findSearched error: {} {} {}", query, page, e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }

        List<BookEntity> bookEntities =
            allResults.stream().map(r -> BookEntityConverter.toBookEntityForOwnList(r)).collect(Collectors.toList());

        return BookListEntity.builder().books(bookEntities).sumCount(sumCount).build();
    }

    @Override
    public BookListEntity findRecentlyViewed(String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<String, Object>> allResults;
        try {
            allResults = g.V(userId)
                          .out(EdgeLabel.VIEW.getString())
                          .hasLabel(VertexLabel.BOOK.getString())
                          .order()
                          .by(coalesce(inE(EdgeLabel.VIEW.getString()).values(DateProperty.CREATE_TIME.getString()),
                                       values(DateProperty.CREATE_TIME.getString())), Order.desc)
                          .range(0, 6)
                          .project("base", "author")
                          .by(__.valueMap().with(WithOptions.tokens))
                          .by(__.in(EdgeLabel.PUBLISH.getString(),
                                    EdgeLabel.DRAFT.getString(),
                                    EdgeLabel.SUSPEND.getString(),
                                    EdgeLabel.DELETE.getString())
                                .hasLabel(VertexLabel.USER.getString())
                                .valueMap()
                                .with(WithOptions.tokens))
                          .toList();
        } catch (Exception e) {
            log.error("findRecentlyViewed error: {} {}", userId, e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }

        List<BookEntity> bookEntities =
            allResults.stream().map(r -> BookEntityConverter.toBookEntityForOwnList(r)).collect(Collectors.toList());

        return BookListEntity.builder().books(bookEntities).sumCount(6).build();
    }

    @Override
    public BookListEntity findNew(int page) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<String, Object>> allResults;
        long sumCount;
        try {
            allResults = g.V()
                          .out(EdgeLabel.PUBLISH.getString())
                          .hasLabel(VertexLabel.BOOK.getString())
                          .order()
                          .by(coalesce(inE(EdgeLabel.PUBLISH.getString()).values(DateProperty.CREATE_TIME.getString()),
                                       values(DateProperty.CREATE_TIME.getString())), Order.desc)
                          .range(12 * (page - 1), 12 * page)
                          .project("base", "author")
                          .by(__.valueMap().with(WithOptions.tokens))
                          .by(__.in(EdgeLabel.PUBLISH.getString(),
                                    EdgeLabel.DRAFT.getString(),
                                    EdgeLabel.SUSPEND.getString(),
                                    EdgeLabel.DELETE.getString())
                                .hasLabel(VertexLabel.USER.getString())
                                .valueMap()
                                .with(WithOptions.tokens))
                          .toList();

            sumCount = g.E().hasLabel(EdgeLabel.PUBLISH.getString()).count().next();
        } catch (Exception e) {
            log.error("findNew error: {} {}", page, e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }

        List<BookEntity> bookEntities =
            allResults.stream().map(r -> BookEntityConverter.toBookEntityForOwnList(r)).collect(Collectors.toList());

        return BookListEntity.builder().books(bookEntities).sumCount(sumCount).build();
    }

    @Override
    public BookListEntity findFamous(int page) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long sumCount;
        List<Map<String, Object>> allResults;
        try {
            allResults = g.V()
                          .out(EdgeLabel.PUBLISH.getString())
                          .hasLabel(VertexLabel.BOOK.getString())
                          .has(BookVertexProperty.PRICE.getString(), P.gt(0))
                          .project("base", "purchaseCount", "author")
                          .by(__.valueMap().with(WithOptions.tokens))
                          .by(__.in(EdgeLabel.PURCHASE.getString()).count())
                          .by(__.in(EdgeLabel.PUBLISH.getString(),
                                    EdgeLabel.DRAFT.getString(),
                                    EdgeLabel.SUSPEND.getString(),
                                    EdgeLabel.DELETE.getString())
                                .hasLabel(VertexLabel.USER.getString())
                                .valueMap()
                                .with(WithOptions.tokens))
                          .order()
                          .by(select("purchaseCount"), Order.desc)
                          .range(12 * (page - 1), 12 * page)
                          .toList();
            sumCount = g.E().hasLabel(EdgeLabel.PUBLISH.getString()).count().next();
        } catch (Exception e) {
            log.error("findFamous error: {} {}", page, e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }

        List<BookEntity> bookEntities =
            allResults.stream().map(r -> BookEntityConverter.toBookEntityForOwnList(r)).collect(Collectors.toList());


        return BookListEntity.builder().books(bookEntities).sumCount(sumCount).build();
    }

    @Override
    public BookListEntity findFamousFree(int page) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        List<Map<String, Object>> allResults;
        List<BookEntity> bookEntities;
        long sumCount;

        try {
            allResults = g.V()
                          .out(EdgeLabel.PUBLISH.getString())
                          .hasLabel(VertexLabel.BOOK.getString())
                          .has(BookVertexProperty.PRICE.getString(), P.eq(0))
                          .project("base", "purchaseCount", "author")
                          .by(__.valueMap().with(WithOptions.tokens))
                          .by(__.in(EdgeLabel.PURCHASE.getString()).count())
                          .by(__.in(EdgeLabel.PUBLISH.getString(),
                                    EdgeLabel.DRAFT.getString(),
                                    EdgeLabel.SUSPEND.getString(),
                                    EdgeLabel.DELETE.getString())
                                .hasLabel(VertexLabel.USER.getString())
                                .valueMap()
                                .with(WithOptions.tokens))
                          .order()
                          .by(select("purchaseCount"), Order.desc)
                          .range(12 * (page - 1), 12 * page)
                          .toList();

            bookEntities = allResults.stream()
                                     .map(r -> BookEntityConverter.toBookEntityForOwnList(r))
                                     .collect(Collectors.toList());

            sumCount = g.E().hasLabel(EdgeLabel.PUBLISH.getString()).count().next();

        } catch (Exception e) {
            log.error("findFamousFree error: {} {}", page, e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }

        return BookListEntity.builder().books(bookEntities).sumCount(sumCount).build();
    }

    @Override
    public String findAuthorId(String bookId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        try {
            return (String) g.V(bookId)
                             .hasLabel(VertexLabel.BOOK.getString())
                             .in(EdgeLabel.PUBLISH.getString(),
                                 EdgeLabel.DRAFT.getString(),
                                 EdgeLabel.SUSPEND.getString())
                             .id()
                             .next();
        } catch (Exception e) {
            log.error("findAuthorId error: {} {}", bookId, e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }

    @Override
    public long findSectionCount(String bookId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        try {
            return g.V(bookId)
                    .hasLabel(VertexLabel.BOOK.getString())
                    .out(EdgeLabel.INCLUDE.getString())
                    .hasLabel(VertexLabel.SECTION.getString())
                    .count()
                    .next();
        } catch (Exception e) {
            log.error("findSectionCount error: {} {}", bookId, e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }

    }

    @Override
    public long findPageCount(String sectionId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        try {
            return g.V(sectionId)
                    .hasLabel(VertexLabel.SECTION.getString())
                    .out(EdgeLabel.INCLUDE.getString())
                    .hasLabel(VertexLabel.PAGE.getString())
                    .count()
                    .next();
        } catch (Exception e) {
            log.error("findPageCount error: {} {}", sectionId, e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }

    @Override
    public List<String> findSectionIds(String bookId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        try {
            return g.V(bookId)
                    .hasLabel(VertexLabel.BOOK.getString())
                    .out(EdgeLabel.INCLUDE.getString())
                    .hasLabel(VertexLabel.SECTION.getString())
                    .id()
                    .toList()
                    .stream()
                    .map(id -> (String) id)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("findSectionIds error: {} {}", bookId, e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }

    @Override
    public List<String> findPageIds(String sectionId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        try {
            return g.V(sectionId)
                    .hasLabel(VertexLabel.SECTION.getString())
                    .out(EdgeLabel.INCLUDE.getString())
                    .hasLabel(VertexLabel.PAGE.getString())
                    .id()
                    .toList()
                    .stream()
                    .map(id -> (String) id)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("findPageIds error: {} {}", sectionId, e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }

    @Override
    public boolean isPurchased(String bookId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        try {
            return g.V(bookId)
                    .hasLabel(VertexLabel.BOOK.getString())
                    .coalesce(__.inE(EdgeLabel.PURCHASE.getString())
                                .where(outV().hasId(userId).hasLabel(VertexLabel.USER.getString()))
                                .limit(1)
                                .constant(true), constant(false))
                    .next();
        } catch (Exception e) {
            log.error("isPurchased error: {} {} {}", bookId, userId, e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }

    @Override
    public boolean isPublished(String bookId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        try {
            return g.V(bookId)
                    .hasLabel(VertexLabel.BOOK.getString())
                    .coalesce(__.inE(EdgeLabel.PUBLISH.getString())
                                .where(outV().hasLabel(VertexLabel.USER.getString()))
                                .limit(1)
                                .constant(true), constant(false))
                    .next();
        } catch (Exception e) {
            log.error("isPurchased error: {} {}", bookId, e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }

    @Override
    public int findPrice(String bookId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        try {
            return (int) g.V(bookId)
                          .hasLabel(VertexLabel.BOOK.getString())
                          .values(BookVertexProperty.PRICE.getString())
                          .next();
        } catch (Exception e) {
            log.error("findPrice error: {} {}", bookId, e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }

    @Override
    public String findStatus(String bookId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        try {
            return g.V(bookId)
                    .hasLabel(VertexLabel.BOOK.getString())
                    .inE(EdgeLabel.PUBLISH.getString(),
                         EdgeLabel.DRAFT.getString(),
                         EdgeLabel.SUSPEND.getString(),
                         EdgeLabel.DELETE.getString())
                    .where(__.outV().hasLabel(VertexLabel.USER.getString()))
                    .label()
                    .next();
        } catch (Exception e) {
            log.error("findStatus error: {} {}", bookId, e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }
}
