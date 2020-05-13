package jp.boosty.graphql.domain.service.book;

import com.stripe.exception.StripeException;

import jp.boosty.graphql.application.datamodel.request.book.BookBaseInput;
import jp.boosty.graphql.application.datamodel.request.book.BookTargetsInput;
import jp.boosty.graphql.application.datamodel.request.user.Requester;
import jp.boosty.graphql.domain.GraphQLCustomException;
import jp.boosty.graphql.domain.domainmodel.book.Book;
import jp.boosty.graphql.domain.domainmodel.book.BookBase;
import jp.boosty.graphql.domain.domainmodel.book.BookFeature;
import jp.boosty.graphql.domain.domainmodel.book.BookFeatures;
import jp.boosty.graphql.domain.domainmodel.book.BookTargets;
import jp.boosty.graphql.domain.domainmodel.book.section.BookSectionTitle;
import jp.boosty.graphql.domain.domainmodel.content.ContentImageUrl;
import jp.boosty.graphql.domain.domainmodel.content.ContentTags;
import jp.boosty.graphql.domain.domainmodel.user.UserId;
import jp.boosty.graphql.domain.factory.BookBaseFactory;
import jp.boosty.graphql.domain.factory.BookFactory;
import jp.boosty.graphql.domain.factory.BookTargetsFactory;
import jp.boosty.graphql.infrastructure.constant.GraphQLErrorMessage;
import jp.boosty.graphql.infrastructure.constant.payment.PaymentStatus;
import jp.boosty.graphql.infrastructure.datamodel.book.BookEntity;
import jp.boosty.graphql.infrastructure.datamodel.payment.PaymentEntity;
import jp.boosty.graphql.infrastructure.datamodel.user.UserEntity;
import jp.boosty.graphql.infrastructure.repository.book.BookMutationRepositoryImpl;
import jp.boosty.graphql.infrastructure.repository.book.BookQueryRepositoryImpl;
import jp.boosty.graphql.infrastructure.repository.payment.PaymentRepositoryImpl;
import jp.boosty.graphql.infrastructure.repository.user.UserQueryRepositoryImpl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("bookMutation")
public class BookMutationService {
    private final BookMutationRepositoryImpl bookMutationRepository;
    private final BookQueryRepositoryImpl bookQueryRepository;
    private final UserQueryRepositoryImpl userQueryRepository;
    private final PaymentRepositoryImpl paymentRepository;

    public BookMutationService(
        BookMutationRepositoryImpl bookMutationRepository,
        BookQueryRepositoryImpl bookQueryRepository,
        UserQueryRepositoryImpl userQueryRepository,
        PaymentRepositoryImpl paymentRepository) {
        this.bookMutationRepository = bookMutationRepository;
        this.bookQueryRepository = bookQueryRepository;
        this.userQueryRepository = userQueryRepository;
        this.paymentRepository = paymentRepository;
    }


    public void publish(String bookId, Requester requester) {
        log.info("publish bookId: {}", bookId);

        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        if (!isOwner(bookId, requester.getUserId())) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        BookEntity bookEntity = bookQueryRepository.findOneForGuest(bookId);
        Book book = BookFactory.make(bookEntity);
        book.publishValidation();


        bookMutationRepository.publish(bookId, book, requester.getUserId());
    }

    public void suspend(String bookId, Requester requester) {
        log.info("suspend bookId: {}", bookId);

        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        if (!isOwner(bookId, requester.getUserId())) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        bookMutationRepository.suspend(bookId, requester.getUserId());
    }

    public void purchase(String paymentIntentId, Requester requester) {
        log.info("purchase paymentIntentId: {}", paymentIntentId);

        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        PaymentEntity paymentEntity;
        try {
            paymentEntity = paymentRepository.findOne(paymentIntentId);
        } catch (StripeException e) {
            log.error(e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }

        //https://stripe.com/docs/api/payment_intents/object#payment_intent_object-status
        if (!paymentEntity.getStatus().equals(PaymentStatus.SUCCEEDED.getString())) {
            log.error("payment status: {}", paymentEntity.getStatus());
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.PAYMENT_NOT_SUCCEEDED.getString());
        }

        if (!paymentEntity.getPurchaseUserId().equals(requester.getUserId())) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        if (bookQueryRepository.isPurchased(paymentEntity.getBookId(), requester.getUserId())) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.ALREADY_PURCHASED.getString());
        }

        bookMutationRepository.purchase(paymentEntity, requester.getUserId(), paymentIntentId);
    }

    public void addBookShelf(String bookId, Requester requester) {
        log.info("purchase bookId: {}", bookId);

        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        int price = bookQueryRepository.findPrice(bookId);
        if (price > 0) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.NEED_PURCHASE.getString());
        }

        if (bookQueryRepository.isPurchased(bookId, requester.getUserId())) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.ALREADY_PURCHASED.getString());
        }

        bookMutationRepository.addBookShelf(bookId, requester.getUserId());
    }

    public String create(Requester requester) {
        log.info("create book requesterId: {}", requester.getUserId());

        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        return bookMutationRepository.create(requester.getUserId());
    }

    public void updateBase(String bookId, BookBaseInput bookBaseInput, Requester requester) {
        log.info("update book base info: {} {}", bookBaseInput, bookId);
        validateAuthor(bookId, requester);

        BookBase base = BookBaseFactory.make(bookBaseInput);

        UserEntity userEntity = userQueryRepository.findOne(requester.getUserId());
        if (!base.canSale(userEntity)) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.NEED_REGISTER_TO_SALE.getString());
        }

        bookMutationRepository.updateBase(bookId, base);
    }

    public void updateImageUrl(String bookId, String imageUrl, Requester requester) {
        log.info("update book imageUrl: {} {}", imageUrl, bookId);
        validateAuthor(bookId, requester);

        ContentImageUrl url = ContentImageUrl.of(imageUrl);

        bookMutationRepository.updateImageUrl(bookId, url.getValue());
    }

    public void updateFeatures(String bookId, List<String> features, Requester requester) {
        log.info("update book features: {} {}", features, bookId);
        validateAuthor(bookId, requester);

        BookFeatures bookFeatures =
            BookFeatures.of(features.stream().map(f -> BookFeature.of(f)).collect(Collectors.toList()));

        bookMutationRepository.updateFeatures(bookId, bookFeatures);
    }

    public void updateTargets(String bookId, BookTargetsInput targetsInput, Requester requester) {
        log.info("update book target: {} {}", targetsInput, bookId);
        validateAuthor(bookId, requester);


        BookTargets bookTargets = BookTargetsFactory.make(targetsInput);

        bookMutationRepository.updateTargets(bookId, bookTargets);
    }

    public void updateTags(String bookId, List<String> tags, Requester requester) {
        log.info("update book tags: {} {}", tags, bookId);
        validateAuthor(bookId, requester);

        ContentTags contentTags = ContentTags.of(tags);

        bookMutationRepository.updateTags(bookId, contentTags);
    }

    public String addSection(String bookId, String title, Requester requester) {
        log.info("add section title: {} {}", title, bookId);
        validateAuthor(bookId, requester);

        BookSectionTitle sectionName = BookSectionTitle.of(title);

        long sectionCount = bookQueryRepository.findSectionCount(bookId);
        if (sectionCount >= 19) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.SECTION_SIZE_OVER.getString());
        }

        return bookMutationRepository.addSection(bookId, sectionName, sectionCount + 1);
    }

    public void updateSectionTitle(String bookId, String sectionId, String title, Requester requester) {
        log.info("update section title: {} {} {}", title, sectionId, bookId);
        validateAuthor(bookId, requester);

        BookSectionTitle sectionName = BookSectionTitle.of(title);

        bookMutationRepository.updateSectionTitle(sectionId, sectionName);
    }

    public void reorderSections(String bookId, List<String> sectionIds, Requester requester) {
        log.info("reorder sections sectionIds: {} {}", sectionIds, bookId);
        validateAuthor(bookId, requester);

        List<String> originalIds = bookQueryRepository.findSectionIds(bookId);

        boolean sameIdByOrigin = originalIds.stream().allMatch(o -> sectionIds.stream().anyMatch(s -> o.equals(s)));
        boolean sameIdByInput = sectionIds.stream().allMatch(s -> originalIds.stream().anyMatch(o -> s.equals(o)));

        if (!(sameIdByInput && sameIdByOrigin)) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.INVALID_SECTION_DATA.getString());
        }

        bookMutationRepository.reorderSections(bookId, sectionIds);
    }

    public void reorderPages(String bookId, String sectionId, List<String> pageIds, Requester requester) {
        log.info("reorder page pageIds: {} {} {}", pageIds, sectionId, bookId);
        validateAuthor(bookId, requester);

        List<String> originalIds = bookQueryRepository.findPageIds(sectionId);

        boolean sameIdByOrigin = originalIds.stream().allMatch(o -> pageIds.stream().anyMatch(s -> o.equals(s)));
        boolean sameIdByInput = pageIds.stream().allMatch(s -> originalIds.stream().anyMatch(o -> s.equals(o)));

        if (!(sameIdByInput && sameIdByOrigin)) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.INVALID_PAGE_DATA.getString());
        }

        bookMutationRepository.reorderPages(sectionId, pageIds);
    }

    public String createPage(String bookId, String sectionId, Requester requester) {
        log.info("create page to: {} {}", sectionId, bookId);
        validateAuthor(bookId, requester);

        long pageCount = bookQueryRepository.findPageCount(sectionId);
        if (pageCount >= 19) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.PAGE_SIZE_OVER.getString());
        }

        return bookMutationRepository.createPage(sectionId, pageCount + 1, requester.getUserId());
    }

    public void deleteSection(String bookId, String sectionId, Requester requester) {
        log.info("delete section id: {} {}", sectionId, bookId);
        validateAuthor(bookId, requester);

        bookMutationRepository.deleteSection(bookId, sectionId);
    }

    public void delete(String bookId, Requester requester) {
        log.info("delete articleId: {}", bookId);

        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        if (!isOwner(bookId, requester.getUserId())) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        bookMutationRepository.delete(bookId, requester.getUserId());
    }


    private boolean isOwner(String bookId, String userId) {
        // 新規投稿じゃなければ、更新できるユーザーかチェックする
        UserId authorId = UserId.of(bookQueryRepository.findAuthorId(bookId));
        UserId requesterId = UserId.of(userId);
        return authorId.same(requesterId);
    }

    private void validateAuthor(String bookId, Requester requester) {
        if (StringUtil.isNullOrEmpty(bookId)) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.INVALID_BOOK_DATA.getString());
        }

        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        if (!isOwner(bookId, requester.getUserId())) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.NOT_AUTHORIZED.getString());
        }
    }
}
