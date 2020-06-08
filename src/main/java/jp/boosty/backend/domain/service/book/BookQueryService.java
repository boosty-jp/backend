package jp.boosty.backend.domain.service.book;

import com.stripe.exception.StripeException;

import jp.boosty.backend.application.datamodel.request.search.SearchConditionInput;
import jp.boosty.backend.application.datamodel.request.user.Requester;
import jp.boosty.backend.application.datamodel.response.query.book.PaymentIntentResponse;
import jp.boosty.backend.infrastructure.datamodel.book.BookListEntity;
import jp.boosty.backend.infrastructure.repository.book.BookQueryRepositoryImpl;
import jp.boosty.backend.infrastructure.repository.payment.PaymentRepositoryImpl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jp.boosty.backend.domain.GraphQLCustomException;
import jp.boosty.backend.domain.domainmodel.search.SearchCondition;
import jp.boosty.backend.domain.factory.SearchConditionFactory;
import jp.boosty.backend.infrastructure.constant.GraphQLErrorMessage;
import jp.boosty.backend.infrastructure.constant.edge.EdgeLabel;
import jp.boosty.backend.infrastructure.datamodel.book.BookEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BookQueryService {
    private final BookQueryRepositoryImpl bookQueryRepository;
    private final PaymentRepositoryImpl paymentRepository;

    public BookQueryService(
        BookQueryRepositoryImpl bookQueryRepository, PaymentRepositoryImpl paymentRepository) {
        this.bookQueryRepository = bookQueryRepository;
        this.paymentRepository = paymentRepository;
    }

    public BookEntity findBook(String bookId, Requester requester) {
        log.info("find book: {}", bookId);
        BookEntity bookEntity;
        if (requester.isGuest()) {
            bookEntity = bookQueryRepository.findOneForGuest(bookId);
        } else {
            bookEntity = bookQueryRepository.findOne(bookId, requester.getUserId());
        }

        if (!canRead(bookEntity, requester.getUserId())) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        return bookEntity;
    }

    public String findPageIdToRead(String bookId, Requester requester) {
        log.info("find pageId to read: {}", bookId);
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        BookEntity bookEntity = bookQueryRepository.findOne(bookId, requester.getUserId());
        if (bookEntity.getLastViewedPageId().isEmpty()) {
            return bookEntity.getSections().get(0).getPages().get(0).getId();
        }

        return bookEntity.getLastViewedPageId();
    }

    public BookListEntity findCreatedBook(String userId, SearchConditionInput searchConditionInput) {
        log.info("find createdBook: {}", searchConditionInput);
        SearchCondition searchCondition = SearchConditionFactory.makeFilteredPublished(searchConditionInput);

        return bookQueryRepository.findCreated(userId, searchCondition);
    }

    public BookListEntity findCreatedBooksBySelf(
        Requester requester, SearchConditionInput searchConditionInput) {
        log.info("find createdBook by self: {}", searchConditionInput);
        SearchCondition searchCondition = SearchConditionFactory.make(searchConditionInput);

        if (!searchCondition.createdFilter()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_SEARCH_CONDITION.getString());
        }

        return bookQueryRepository.findCreatedBySelf(requester.getUserId(), searchCondition);
    }

    public BookListEntity findOwnBooks(Requester requester, int page) {
        log.info("find own books: {}", page);

        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        return bookQueryRepository.findOwn(requester.getUserId(), page);
    }

    public BookListEntity findSearchedBooks(String query, int page) {
        log.info("search book: {} {}", query, page);
        if (query.isEmpty()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(), GraphQLErrorMessage.NULL_DATA.getString());
        }

        return bookQueryRepository.findSearched(query, page);
    }

    public BookListEntity findRecentlyViewedBooks(Requester requester) {

        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        return bookQueryRepository.findRecentlyViewed(requester.getUserId());
    }


    public BookListEntity findNewBooks(int page) {
        return bookQueryRepository.findNew(page);
    }

    public BookListEntity findFamousBooks(int page) {
        return bookQueryRepository.findFamous(page);
    }

    public BookListEntity findFamousFreeBooks(int page) {
        return bookQueryRepository.findFamousFree(page);
    }

    //TODO: ドメイン化
    private boolean canRead(BookEntity bookEntity, String requesterId) {
        if (bookEntity.getBase().getStatus().equals(EdgeLabel.PUBLISH.getString())) {
            return true;
        }

        if (bookEntity.isPurchased()) {
            return true;
        }

        return requesterId.equals(bookEntity.getAuthor().getUserId());
    }

    public PaymentIntentResponse findPaymentIntent(String bookId, Requester requester) {
        log.info("find payment intent: {}", bookId);
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.UNAUTHORIZED.value(),
                                             GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        BookEntity bookEntity = bookQueryRepository.findOne(bookId, requester.getUserId());
        int price = bookEntity.getBase().getPrice();
        if (bookEntity.isPurchased()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.ALREADY_PURCHASED.getString());
        }

        if (price < 50) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.LESS_PRICE.getString());
        }

        if (!bookEntity.getBase().getStatus().equals(EdgeLabel.PUBLISH.getString())) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NOT_ON_SALE.getString());
        }

        String secret;
        try {
            secret = paymentRepository.createPaymentIntent(price,
                                                           bookEntity.getAuthor().getStripeId(),
                                                           bookId,
                                                           requester.getUserId());
        } catch (StripeException e) {
            log.error(e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
        return PaymentIntentResponse.builder().price(price).secret(secret).build();
    }
}
