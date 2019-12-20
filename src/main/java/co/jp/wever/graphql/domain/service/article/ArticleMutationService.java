package co.jp.wever.graphql.domain.service.article;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import co.jp.wever.graphql.application.datamodel.request.ArticleInput;
import co.jp.wever.graphql.application.datamodel.request.Requester;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.domainmodel.article.Article;
import co.jp.wever.graphql.domain.domainmodel.user.UserId;
import co.jp.wever.graphql.domain.factory.ArticleFactory;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import co.jp.wever.graphql.infrastructure.repository.article.ArticleMutationRepositoryImpl;
import co.jp.wever.graphql.infrastructure.repository.article.ArticleQueryRepositoryImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ArticleMutationService {
    private final ArticleQueryRepositoryImpl articleQueryRepository;
    private final ArticleMutationRepositoryImpl articleMutationRepository;

    public ArticleMutationService(
        ArticleQueryRepositoryImpl articleQueryRepository,
        ArticleMutationRepositoryImpl articleMutationRepository) {
        this.articleQueryRepository = articleQueryRepository;
        this.articleMutationRepository = articleMutationRepository;
    }

    public String publish(ArticleInput articleInput, Requester requester) {
        log.info("publish article: {}", articleInput);

        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        Article article = ArticleFactory.make(articleInput);

        if (isForbiddenAuthor(article, articleInput.getId(), requester.getUserId())) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        if (!article.canPublish()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.CANNOT_PUBLISH_ARTICLE.getString());
        }

        if (article.entry()) {
            return articleMutationRepository.publishByEntry(article, requester.getUserId());
        } else {
            articleMutationRepository.publish(articleInput.getId(), article, requester.getUserId());
            return articleInput.getId();
        }
    }

    public String draft(ArticleInput articleInput, Requester requester) {
        log.info("draft article: {}", articleInput);

        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        Article article = ArticleFactory.make(articleInput);

        if (isForbiddenAuthor(article, articleInput.getId(), requester.getUserId())) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        if (article.entry()) {
            return articleMutationRepository.draftByEntry(article, requester.getUserId());
        } else {
            articleMutationRepository.draft(articleInput.getId(), article, requester.getUserId());
            return articleInput.getId();
        }
    }

    public void like(String articleId, Requester requester) {
        log.info("like articleId: {}", articleId);
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        articleMutationRepository.like(articleId, requester.getUserId());
    }

    public void clearLike(String articleId, Requester requester) {
        log.info("delete articleId: {}", articleId);
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }
        articleMutationRepository.clearLike(articleId, requester.getUserId());
    }

    public void learn(String articleId, Requester requester) {
        log.info("finish articleId: {}", articleId);

        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }
        articleMutationRepository.learn(articleId, requester.getUserId());
    }

    public void clearLearn(String articleId, Requester requester) {
        log.info("delete finish articleId: {}", articleId);
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }
        articleMutationRepository.clearLearn(articleId, requester.getUserId());
    }

    public void delete(String articleId, Requester requester) {
        log.info("delete articleId: {}", articleId);

        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        if (!isOwner(articleId, requester.getUserId())) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        articleMutationRepository.delete(articleId, requester.getUserId());
    }

    private boolean isForbiddenAuthor(Article article, String articleId, String userId) {
        if (article.entry()) {
            return false;
        }

        return !isOwner(articleId, userId);
    }

    private boolean isOwner(String articleId, String userId) {
        // 新規投稿じゃなければ、更新できるユーザーかチェックする
        UserId authorId = UserId.of(articleQueryRepository.findAuthorId(articleId));
        UserId requesterId = UserId.of(userId);
        return authorId.same(requesterId);
    }
}
