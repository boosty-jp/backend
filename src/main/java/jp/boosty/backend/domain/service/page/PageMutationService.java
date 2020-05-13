package jp.boosty.backend.domain.service.page;

import jp.boosty.backend.application.datamodel.request.page.PageInput;
import jp.boosty.backend.application.datamodel.request.user.Requester;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jp.boosty.backend.domain.GraphQLCustomException;
import jp.boosty.backend.domain.domainmodel.page.Page;
import jp.boosty.backend.domain.domainmodel.user.UserId;
import jp.boosty.backend.domain.factory.PageFactory;
import jp.boosty.backend.infrastructure.constant.GraphQLErrorMessage;
import jp.boosty.backend.infrastructure.repository.page.PageMutationRepositoryImpl;
import jp.boosty.backend.infrastructure.repository.page.PageQueryRepositoryImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PageMutationService {
    private final PageQueryRepositoryImpl pageQueryRepository;
    private final PageMutationRepositoryImpl pageMutationRepository;

    public PageMutationService(
        PageQueryRepositoryImpl pageQueryRepository, PageMutationRepositoryImpl pageMutationRepository) {
        this.pageQueryRepository = pageQueryRepository;
        this.pageMutationRepository = pageMutationRepository;
    }

    public void save(String bookId, String pageId, PageInput pageInput, Requester requester) {
        log.info("save page: {} {} {}", pageInput, pageId, bookId);

        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        Page page = PageFactory.make(pageInput);

        if (!isOwner(pageId, requester.getUserId())) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        pageMutationRepository.save(bookId, pageId, page);
    }

    public void delete(String pageId, Requester requester) {
        log.info("delete pageId: {}", pageId);

        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        if (!isOwner(pageId, requester.getUserId())) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        pageMutationRepository.delete(pageId, requester.getUserId());
    }

    public void like(String pageId, Requester requester) {
        log.info("like pageId: {}", pageId);

        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        pageMutationRepository.like(pageId, requester.getUserId());
    }

    public void unLike(String pageId, Requester requester) {
        log.info("unLike pageId: {}", pageId);

        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        pageMutationRepository.unLike(pageId, requester.getUserId());
    }

    public void updateTrialRead(String pageId, boolean canPreview, Requester requester) {
        log.info("update trial read: {} {}", canPreview, pageId);

        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        if (!isOwner(pageId, requester.getUserId())) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        pageMutationRepository.updateTrialRead(pageId, canPreview, requester.getUserId());
    }

    private boolean isOwner(String pageId, String userId) {
        // 新規投稿じゃなければ、更新できるユーザーかチェックする
        UserId authorId = UserId.of(pageQueryRepository.findAuthorId(pageId));
        UserId requesterId = UserId.of(userId);
        return authorId.same(requesterId);
    }
}
