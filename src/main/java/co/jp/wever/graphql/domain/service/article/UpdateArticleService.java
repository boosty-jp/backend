package co.jp.wever.graphql.domain.service.article;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import co.jp.wever.graphql.application.datamodel.request.ArticleInput;
import co.jp.wever.graphql.application.datamodel.request.Requester;
import co.jp.wever.graphql.application.datamodel.request.UpdateSectionInput;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.converter.article.DraftArticleConverter;
import co.jp.wever.graphql.domain.converter.article.PublishArticleConverter;
import co.jp.wever.graphql.domain.domainmodel.article.DraftArticle;
import co.jp.wever.graphql.domain.domainmodel.article.PublishArticle;
import co.jp.wever.graphql.domain.domainmodel.article.base.ArticleImageUrl;
import co.jp.wever.graphql.domain.domainmodel.article.base.ArticleTitle;
import co.jp.wever.graphql.domain.domainmodel.user.UserId;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import co.jp.wever.graphql.infrastructure.repository.article.FindArticleRepositoryImpl;
import co.jp.wever.graphql.infrastructure.repository.article.UpdateArticleRepositoryImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UpdateArticleService {
    private final FindArticleRepositoryImpl findArticleRepository;
    private final UpdateArticleRepositoryImpl updateArticleRepository;

    public UpdateArticleService(
        FindArticleRepositoryImpl findArticleRepository, UpdateArticleRepositoryImpl updateArticleRepository) {
        this.findArticleRepository = findArticleRepository;
        this.updateArticleRepository = updateArticleRepository;
    }

    public void updateArticleTitle(String articleId, Requester requester, String title) {
        log.info("update articleId: {}", articleId);
        log.info("update article title: {}", title);

        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        UserId authorId = UserId.of(findArticleRepository.findAuthorId(articleId));
        UserId requesterId = UserId.of(requester.getUserId());

        if (!authorId.same(requesterId)) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        ArticleTitle articleTitle = ArticleTitle.of(title);

        updateArticleRepository.updateTitle(articleId, articleTitle.getValue());
    }

    public void updateArticleImageUrl(String articleId, Requester requester, String imageUrl) {
        log.info("update articleId: {}", articleId);
        log.info("update article imageUrl: {}", imageUrl);

        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        UserId authorId = UserId.of(findArticleRepository.findAuthorId(articleId));
        UserId requesterId = UserId.of(requester.getUserId());

        if (!authorId.same(requesterId)) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        ArticleImageUrl articleImageUrl = ArticleImageUrl.of(imageUrl);
        updateArticleRepository.updateImageUrl(articleId, articleImageUrl.getValue());
    }

    public void updateArticleTags(String articleId, Requester requester, List<String> tags) {
        log.info("update articleId: {}", articleId);
        log.info("update article tags: {}", tags);

        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        // TODO: ドメインに移動させる
        if (tags.size() > 5) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_TAG_COUNT.getString());
        }

        UserId authorId = UserId.of(findArticleRepository.findAuthorId(articleId));
        UserId requesterId = UserId.of(requester.getUserId());

        if (!authorId.same(requesterId)) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        updateArticleRepository.updateTags(articleId, tags);
    }

    public void publishArticle(ArticleInput articleInput, List<UpdateSectionInput> sectionInputs, Requester requester) {
        log.info("publish article: {}", articleInput);
        log.info("publish section size: {}", sectionInputs.size());

        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }
        UserId authorId = UserId.of(findArticleRepository.findAuthorId(articleInput.getId()));
        UserId requesterId = UserId.of(requester.getUserId());

        if (!authorId.same(requesterId)) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        PublishArticle publishArticle = PublishArticleConverter.toPublishArticle(articleInput, sectionInputs);

        updateArticleRepository.publishOne(publishArticle, authorId.getValue());
    }

    public void draftArticle(ArticleInput articleInput, List<UpdateSectionInput> sectionInputs, Requester requester) {
        log.info("draft article: {}", articleInput);
        log.info("publish section size: {}", sectionInputs.size());

        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }
        UserId authorId = UserId.of(findArticleRepository.findAuthorId(articleInput.getId()));
        UserId requesterId = UserId.of(requester.getUserId());

        if (!authorId.same(requesterId)) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        DraftArticle draftArticle = DraftArticleConverter.toDraftArticle(articleInput, sectionInputs);

        updateArticleRepository.draftOne(draftArticle, authorId.getValue());
    }

    public void likeArticle(String articleId, Requester requester) {
        log.info("like articleId: {}", articleId);
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }
        updateArticleRepository.likeOne(articleId, requester.getUserId());
    }

    public void deleteLikeArticle(String articleId, Requester requester) {
        log.info("delete articleId: {}", articleId);
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }
        updateArticleRepository.deleteLikeOne(articleId, requester.getUserId());
    }

    public void finishArticle(String articleId, Requester requester) {
        log.info("finish articleId: {}", articleId);

        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }
        updateArticleRepository.finishOne(articleId, requester.getUserId());
    }

    public void deleteFinishArticle(String articleId, Requester requester) {
        log.info("delete finish articleId: {}", articleId);
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }
        updateArticleRepository.deleteFinishOne(articleId, requester.getUserId());
    }
}
