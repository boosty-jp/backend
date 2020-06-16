package jp.boosty.backend.infrastructure.repository.book;

import jp.boosty.backend.domain.GraphQLCustomException;
import jp.boosty.backend.domain.domainmodel.book.Book;
import jp.boosty.backend.domain.domainmodel.book.BookBase;
import jp.boosty.backend.domain.domainmodel.book.BookFeatures;
import jp.boosty.backend.domain.domainmodel.book.BookTargets;
import jp.boosty.backend.domain.domainmodel.book.section.BookSectionTitle;
import jp.boosty.backend.domain.domainmodel.content.ContentTags;
import jp.boosty.backend.domain.repository.book.BookMutationRepository;
import jp.boosty.backend.infrastructure.connector.NeptuneClient;
import jp.boosty.backend.infrastructure.constant.GraphQLErrorMessage;
import jp.boosty.backend.infrastructure.constant.edge.EdgeLabel;
import jp.boosty.backend.infrastructure.constant.edge.property.IncludeEdgeProperty;
import jp.boosty.backend.infrastructure.constant.edge.property.PurchaseEdgeProperty;
import jp.boosty.backend.infrastructure.constant.vertex.label.VertexLabel;
import jp.boosty.backend.infrastructure.constant.vertex.property.BookFeatureProperty;
import jp.boosty.backend.infrastructure.constant.vertex.property.BookTargetDescriptionProperty;
import jp.boosty.backend.infrastructure.constant.vertex.property.BookVertexProperty;
import jp.boosty.backend.infrastructure.constant.vertex.property.DateProperty;
import jp.boosty.backend.infrastructure.constant.vertex.property.PageVertexProperty;
import jp.boosty.backend.infrastructure.constant.vertex.property.SectionVertexProperty;
import jp.boosty.backend.infrastructure.constant.vertex.property.TagVertexProperty;
import jp.boosty.backend.infrastructure.datamodel.payment.PaymentEntity;
import jp.boosty.backend.infrastructure.util.EdgeIdCreator;

import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.T;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.inV;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.outV;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.unfold;
import static org.apache.tinkerpop.gremlin.structure.VertexProperty.Cardinality.single;

@Slf4j
@Component
public class BookMutationRepositoryImpl implements BookMutationRepository {

    private final NeptuneClient neptuneClient;

    public BookMutationRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    @Override
    public String create(String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        String bookId;
        try {
            bookId = g.addV(VertexLabel.BOOK.getString())
                      .property(BookVertexProperty.TITLE.getString(), "")
                      .property(BookVertexProperty.DESCRIPTION.getString(), "")
                      .property(BookVertexProperty.PRICE.getString(), 0)
                      .property(BookVertexProperty.LEVEL_START.getString(), 0)
                      .property(BookVertexProperty.LEVEL_END.getString(), 0)
                      .property(DateProperty.CREATE_TIME.getString(), now)
                      .property(DateProperty.UPDATE_TIME.getString(), now)
                      .next()
                      .id()
                      .toString();

            addStatus(g, bookId, EdgeLabel.DRAFT.getString(), userId, now);
        } catch (Exception e) {
            log.error("create book error: {}", e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }

        return bookId;
    }

    @Override
    public void updateBase(String bookId, BookBase base) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        try {
            g.V(bookId)
             .hasLabel(VertexLabel.BOOK.getString())
             .property(single, BookVertexProperty.TITLE.getString(), base.getTitle().getValue())
             .property(single, BookVertexProperty.DESCRIPTION.getString(), base.getDescription().getValue())
             .property(single, BookVertexProperty.PRICE.getString(), base.getPrice().getValue())
             .property(single, DateProperty.CREATE_TIME.getString(), now)
             .property(single, DateProperty.UPDATE_TIME.getString(), now)
             .next();
        } catch (Exception e) {
            log.error("update base error: {}", e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }

    @Override
    public void updateImageUrl(String bookId, String imageUrl) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        try {
            g.V(bookId)
             .hasLabel(VertexLabel.BOOK.getString())
             .property(single, BookVertexProperty.IMAGE_URL.getString(), imageUrl)
             .property(single, DateProperty.CREATE_TIME.getString(), now)
             .property(single, DateProperty.UPDATE_TIME.getString(), now)
             .next();
        } catch (Exception e) {
            log.error("update image url error: {}", e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }

    }

    @Override
    public void updateFeatures(String bookId, BookFeatures features) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        try {
            g.V(bookId)
             .hasLabel(VertexLabel.BOOK.getString())
             .out(EdgeLabel.INCLUDE.getString())
             .hasLabel(VertexLabel.BOOK_FEATURE.getString())
             .drop()
             .iterate();

            AtomicInteger idx = new AtomicInteger();
            features.getFeatures().forEach(f -> {
                String featureId = g.addV(VertexLabel.BOOK_FEATURE.getString())
                                    .property(BookFeatureProperty.TEXT.getString(), f.getFeature())
                                    .property(BookFeatureProperty.NUMBER.getString(), idx.get())
                                    .next()
                                    .id()
                                    .toString();

                g.V(featureId)
                 .addE(EdgeLabel.INCLUDE.getString())
                 .property(DateProperty.CREATE_TIME.getString(), now)
                 .from(g.V(bookId).hasLabel(VertexLabel.BOOK.getString()))
                 .next();
                idx.getAndIncrement();
            });
        } catch (Exception e) {
            log.error("update features error: {}", e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }

    }

    @Override
    public void updateTargets(String bookId, BookTargets targets) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        try {
            g.V(bookId)
             .hasLabel(VertexLabel.BOOK.getString())
             .property(single, BookVertexProperty.LEVEL_START.getString(), targets.getLevel().getStart())
             .property(single, BookVertexProperty.LEVEL_END.getString(), targets.getLevel().getEnd())
             .next();

            g.V(bookId)
             .hasLabel(VertexLabel.BOOK.getString())
             .out(EdgeLabel.INCLUDE.getString())
             .hasLabel(VertexLabel.BOOK_TARGET_DESCRIPTION.getString())
             .drop()
             .iterate();

            AtomicInteger idx = new AtomicInteger();
            targets.getDescriptions().getDescriptions().forEach(d -> {
                String descriptionId = g.addV(VertexLabel.BOOK_TARGET_DESCRIPTION.getString())
                                        .property(BookTargetDescriptionProperty.TEXT.getString(), d.getDescription())
                                        .property(BookTargetDescriptionProperty.NUMBER.getString(), idx.get())
                                        .next()
                                        .id()
                                        .toString();

                g.V(descriptionId)
                 .addE(EdgeLabel.INCLUDE.getString())
                 .property(DateProperty.CREATE_TIME.getString(), now)
                 .from(g.V(bookId).hasLabel(VertexLabel.BOOK.getString()))
                 .next();
                idx.getAndIncrement();
            });
        } catch (Exception e) {
            log.error("update targets error: {}", e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }

    @Override
    public void updateTags(String bookId, ContentTags contentTags) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        try {
            g.V(bookId)
             .hasLabel(VertexLabel.BOOK.getString())
             .outE(EdgeLabel.RELATED_TO.getString())
             .where(inV().hasLabel(VertexLabel.TAG.getString()))
             .drop()
             .iterate();

            List<String> tags = contentTags.getTags().stream().map(t -> t.getValue()).collect(Collectors.toList());
            if (!tags.isEmpty()) {
                tags.forEach(tag -> {
                    String tagId = g.V()
                                    .hasLabel(VertexLabel.TAG.getString())
                                    .has(TagVertexProperty.NAME.getString(), P.eq(tag))
                                    .fold()
                                    .coalesce(unfold(),
                                              g.addV(VertexLabel.TAG.getString())
                                               .property(TagVertexProperty.NAME.getString(), tag))
                                    .next()
                                    .id()
                                    .toString();
                    g.V(tagId)
                     .hasLabel(VertexLabel.TAG.getString())
                     .addE(EdgeLabel.RELATED_TO.getString())
                     .property(DateProperty.CREATE_TIME.getString(), now)
                     .from(g.V(bookId).hasLabel(VertexLabel.BOOK.getString()))
                     .iterate();
                });
            }
        } catch (Exception e) {
            log.error("update tags error: {}", e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }

    @Override
    public void updateMeaningful(String bookId, boolean meaningful) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        try {
            g.V(bookId)
             .hasLabel(VertexLabel.BOOK.getString())
             .property(single, BookVertexProperty.MEANINGFUL.getString(), meaningful)
             .next();
        } catch (Exception e) {
            log.error("update meaningful error: {}", e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }

    @Override
    public String addSection(String bookId, BookSectionTitle name, long sectionCount) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        String sectionId;
        try {
            sectionId = g.addV(VertexLabel.SECTION.getString())
                         .property(SectionVertexProperty.TITLE.getString(), name.getTitle())
                         .property(DateProperty.CREATE_TIME.getString(), now)
                         .property(DateProperty.UPDATE_TIME.getString(), now)
                         .next()
                         .id()
                         .toString();

            g.V(bookId)
             .hasLabel(VertexLabel.BOOK.getString())
             .addE(EdgeLabel.INCLUDE.getString())
             .property(IncludeEdgeProperty.NUMBER.getString(), (int) sectionCount)
             .property(DateProperty.CREATE_TIME.getString(), now)
             .to(g.V(sectionId).hasLabel(VertexLabel.SECTION.getString()))
             .next();
        } catch (Exception e) {
            log.error("add section error: {}", e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }

        return sectionId;
    }

    @Override
    public void updateSectionTitle(String sectionId, BookSectionTitle title) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        try {
            g.V(sectionId)
             .hasLabel(VertexLabel.SECTION.getString())
             .property(single, SectionVertexProperty.TITLE.getString(), title.getTitle())
             .property(single, DateProperty.CREATE_TIME.getString(), now)
             .property(single, DateProperty.UPDATE_TIME.getString(), now)
             .next();
        } catch (Exception e) {
            log.error("update section error: {}", e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }

    @Override
    public void deleteSection(String bookId, String sectionId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        try {
            g.V(bookId)
             .hasLabel(VertexLabel.BOOK.getString())
             .addE(EdgeLabel.DELETE_INCLUDE.getString())
             .property(DateProperty.CREATE_TIME.getString(), now)
             .to(g.V(sectionId).hasLabel(VertexLabel.SECTION.getString()))
             .next();

            g.V(bookId)
             .hasLabel(VertexLabel.BOOK.getString())
             .outE(EdgeLabel.INCLUDE.getString())
             .where(inV().hasId(sectionId).hasLabel(VertexLabel.SECTION.getString()))
             .drop()
             .iterate();
        } catch (Exception e) {
            log.error("delete section error: {}", e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }


    @Override
    public void reorderSections(String bookId, List<String> sectionIds) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        try {

            g.V(bookId)
             .hasLabel(VertexLabel.BOOK.getString())
             .outE(EdgeLabel.INCLUDE.getString())
             .where(inV().hasLabel(VertexLabel.SECTION.getString()))
             .drop()
             .iterate();

            AtomicInteger idx = new AtomicInteger();
            sectionIds.forEach(s -> {
                g.V(s)
                 .hasLabel(VertexLabel.SECTION.getString())
                 .addE(EdgeLabel.INCLUDE.getString())
                 .property(IncludeEdgeProperty.NUMBER.getString(), idx.get())
                 .property(DateProperty.CREATE_TIME.getString(), now)
                 .from(g.V(bookId).hasLabel(VertexLabel.BOOK.getString()))
                 .next();

                idx.getAndIncrement();
            });
        } catch (Exception e) {
            log.error("reorder section error: {}", e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }

    @Override
    public void reorderPages(String sectionId, List<String> pageIds) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        try {
            g.V(sectionId)
             .hasLabel(VertexLabel.SECTION.getString())
             .outE(EdgeLabel.INCLUDE.getString())
             .where(inV().hasLabel(VertexLabel.PAGE.getString()))
             .drop()
             .iterate();

            AtomicInteger idx = new AtomicInteger();
            pageIds.forEach(p -> {
                g.V(p)
                 .hasLabel(VertexLabel.PAGE.getString())
                 .addE(EdgeLabel.INCLUDE.getString())
                 .property(IncludeEdgeProperty.NUMBER.getString(), idx.get())
                 .property(DateProperty.CREATE_TIME.getString(), now)
                 .from(g.V(sectionId).hasLabel(VertexLabel.SECTION.getString()))
                 .next();

                idx.getAndIncrement();
            });
        } catch (Exception e) {
            log.error("reorder pages error: {}", e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }

    @Override
    public String createPage(String sectionId, long sectionCount, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        String pageId;
        try {
            pageId = g.addV(VertexLabel.PAGE.getString())
                      .property(PageVertexProperty.TITLE.getString(), "")
                      .property(PageVertexProperty.TEXT.getString(), "")
                      .property(PageVertexProperty.CAN_PREVIEW.getString(), false)
                      .property(DateProperty.CREATE_TIME.getString(), now)
                      .property(DateProperty.UPDATE_TIME.getString(), now)
                      .next()
                      .id()
                      .toString();

            g.V(sectionId)
             .hasLabel(VertexLabel.SECTION.getString())
             .addE(EdgeLabel.INCLUDE.getString())
             .property(IncludeEdgeProperty.NUMBER.getString(), (int) sectionCount)
             .property(DateProperty.CREATE_TIME.getString(), now)
             .to(g.V(pageId).hasLabel(VertexLabel.PAGE.getString()))
             .next();

            g.V(pageId)
             .hasLabel(VertexLabel.PAGE.getString())
             .addE(EdgeLabel.PUBLISH.getString())
             .property(DateProperty.CREATE_TIME.getString(), now)
             .from(g.V(userId).hasLabel(VertexLabel.USER.getString()))
             .next();
        } catch (Exception e) {
            log.error("reorder pages error: {}", e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }

        return pageId;
    }

    @Override
    public void publish(String bookId, Book book, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        clearStatus(g, bookId, userId);
        addStatus(g, bookId, EdgeLabel.PUBLISH.getString(), userId, now);
    }

    @Override
    public void like(String bookId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        clearLike(g, bookId, userId);

        try {
            g.V(bookId)
             .hasLabel(VertexLabel.BOOK.getString())
             .addE(EdgeLabel.LIKE.getString())
             .property(T.id, EdgeIdCreator.createId(userId, bookId, EdgeLabel.LIKE.getString()))
             .property(DateProperty.CREATE_TIME.getString(), now)
             .from(g.V(userId).hasLabel(VertexLabel.USER.getString()))
             .next();
        } catch (Exception e) {
            log.error("like error: {}", e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }

    @Override
    public void unLike(String bookId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        clearLike(g, bookId, userId);
    }

    @Override
    public void suspend(String bookId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        clearStatus(g, bookId, userId);
        addStatus(g, bookId, EdgeLabel.SUSPEND.getString(), userId, now);
    }

    @Override
    public void delete(String bookId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        clearStatus(g, bookId, userId);
        addStatus(g, bookId, EdgeLabel.DELETE.getString(), userId, now);
    }

    @Override
    public void purchase(PaymentEntity paymentEntity, String userId, String paymentIntentId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        try {
            g.V(paymentEntity.getBookId())
             .hasLabel(VertexLabel.BOOK.getString())
             .addE(EdgeLabel.PURCHASE.getString())
             .property(T.id, EdgeIdCreator.createId(userId, paymentEntity.getBookId(), EdgeLabel.PURCHASE.getString()))
             .property(PurchaseEdgeProperty.PRICE.getString(), paymentEntity.getAmount())
             .property(PurchaseEdgeProperty.PAYMENT_INTENT_ID.getString(), paymentIntentId)
             .property(DateProperty.CREATE_TIME.getString(), now)
             .from(g.V(userId).hasLabel(VertexLabel.USER.getString()))
             .next();
        } catch (Exception e) {
            log.error("purchase error: {}", e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }

    @Override
    public void addBookShelf(String bookId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        try {
            g.V(bookId)
             .hasLabel(VertexLabel.BOOK.getString())
             .addE(EdgeLabel.PURCHASE.getString())
             .property(T.id, EdgeIdCreator.createId(userId, bookId, EdgeLabel.PURCHASE.getString()))
             .property(PurchaseEdgeProperty.PRICE.getString(), 0)
             .property(PurchaseEdgeProperty.PAYMENT_INTENT_ID.getString(), "")
             .property(DateProperty.CREATE_TIME.getString(), now)
             .from(g.V(userId).hasLabel(VertexLabel.USER.getString()))
             .next();
        } catch (Exception e) {
            log.error("add book shelf error: {}", e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }

    private void clearStatus(GraphTraversalSource g, String bookId, String authorId) {
        try {
            g.V(bookId)
             .hasLabel(VertexLabel.BOOK.getString())
             .inE(EdgeLabel.PUBLISH.getString(), EdgeLabel.DRAFT.getString(), EdgeLabel.SUSPEND.getString())
             .where(outV().hasLabel(VertexLabel.USER.getString()).hasId(authorId))
             .drop()
             .iterate();
        } catch (Exception e) {
            log.error("clear status error: {}", e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }

    private void clearLike(GraphTraversalSource g, String bookId, String userId) {
        try {
            g.V(bookId)
             .hasLabel(VertexLabel.BOOK.getString())
             .inE(EdgeLabel.LIKE.getString())
             .where(outV().hasLabel(VertexLabel.USER.getString()).hasId(userId))
             .drop()
             .iterate();
        } catch (Exception e) {
            log.error("clear like error: {}", e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }

    private void addStatus(GraphTraversalSource g, String bookId, String status, String authorId, long now) {
        try {
            g.V(bookId)
             .hasLabel(VertexLabel.BOOK.getString())
             .addE(status)
             .property(T.id, EdgeIdCreator.createId(authorId, bookId, status))
             .property(DateProperty.CREATE_TIME.getString(), now)
             .from(g.V(authorId).hasLabel(VertexLabel.USER.getString()))
             .next();
        } catch (Exception e) {
            log.error("add status error: {} {}", status, e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }
}
