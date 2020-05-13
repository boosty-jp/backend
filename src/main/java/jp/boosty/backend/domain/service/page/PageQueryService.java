package jp.boosty.backend.domain.service.page;

import jp.boosty.backend.application.datamodel.request.search.SearchConditionInput;
import jp.boosty.backend.application.datamodel.request.user.Requester;
import jp.boosty.backend.infrastructure.constant.edge.EdgeLabel;
import jp.boosty.backend.infrastructure.datamodel.page.LikedPageListEntity;
import jp.boosty.backend.infrastructure.repository.book.BookQueryRepositoryImpl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jp.boosty.backend.domain.GraphQLCustomException;
import jp.boosty.backend.domain.domainmodel.search.SearchCondition;
import jp.boosty.backend.domain.factory.SearchConditionFactory;
import jp.boosty.backend.infrastructure.constant.GraphQLErrorMessage;
import jp.boosty.backend.infrastructure.datamodel.page.PageEntity;
import jp.boosty.backend.infrastructure.datamodel.page.PageListEntity;
import jp.boosty.backend.infrastructure.repository.page.PageQueryRepositoryImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PageQueryService {



    private final BookQueryRepositoryImpl bookQueryRepository;
    private final PageQueryRepositoryImpl pageQueryRepository;

    public PageQueryService(
        BookQueryRepositoryImpl bookQueryRepository,
        PageQueryRepositoryImpl pageQueryRepository) {
        this.bookQueryRepository = bookQueryRepository;
        this.pageQueryRepository = pageQueryRepository;
    }

    public PageEntity findPage(String bookId, String pageId, Requester requester) {
        log.info("find page: {} {}", bookId, pageId);
        checkReadable(bookId,pageId,requester);

        if(requester.isGuest()){
            return pageQueryRepository.findOneForGuest(pageId);
        }
        return pageQueryRepository.findOne(bookId, pageId, requester.getUserId());
    }

    public PageEntity findPageToEdit(String pageId, Requester requester) {
        if (!isAuthor(pageId,requester)) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        return pageQueryRepository.findOneToEdit(pageId, requester.getUserId());
    }

    public PageListEntity findCreatedPages(String userId, SearchConditionInput searchConditionInput) {
        log.info("find created page: {}", searchConditionInput);
        SearchCondition searchCondition = SearchConditionFactory.makeFilteredPublished(searchConditionInput);

        return pageQueryRepository.findCreated(userId, searchCondition);
    }

    public PageListEntity findCreatedPagesBySelf(
        Requester requester, SearchConditionInput searchConditionInput) {
        log.info("find created page by self: {}", searchConditionInput);
        SearchCondition searchCondition = SearchConditionFactory.make(searchConditionInput);

        if (!searchCondition.createdFilter()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_SEARCH_CONDITION.getString());
        }


        return pageQueryRepository.findCreatedBySelf(requester.getUserId(), searchCondition);
    }

    public LikedPageListEntity findLikedPages(
        Requester requester, int page) {
        log.info("find liked pages: {}", page);
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        return pageQueryRepository.findLiked(requester.getUserId(), page);
    }

    private boolean needPurchase(String bookId, String pageId, Requester requester) {
        if (pageQueryRepository.canPreview(pageId)) {
            return false;
        }

        return !purchased(bookId, requester);
    }

    private boolean purchased(String bookId, Requester requester) {
        if (requester.isGuest()) {
            return false;
        }

        return bookQueryRepository.isPurchased(bookId, requester.getUserId());
    }

    private boolean isAuthor(String pageId, Requester requester) {
        if (requester.isGuest()) {
            return false;
        }

        return pageQueryRepository.findAuthorId(pageId).equals(requester.getUserId());
    }

    // 読めるステータスでない場合
    private void checkReadable(String bookId, String pageId, Requester requester) {
        String status = bookQueryRepository.findStatus(bookId);
        if (status.equals(EdgeLabel.PUBLISH.getString())) {
            if (needPurchase(bookId, pageId, requester)) {
                throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                                 GraphQLErrorMessage.NEED_PURCHASE.getString());
            }
        } else if (status.equals(EdgeLabel.DRAFT.getString())) {
            if (!isAuthor(bookId, requester)) {
                throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                                 GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
            }
        } else if (status.equals(EdgeLabel.SUSPEND.getString())) {
            if (!purchased(bookId, requester) && !isAuthor(pageId, requester)) {
                throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                                 GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
            }
        } else if (status.equals(EdgeLabel.DELETE.getString())) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }
    }
}
