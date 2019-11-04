package co.jp.wever.graphql.domain.service.article;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import co.jp.wever.graphql.application.datamodel.request.ArticleInput;
import co.jp.wever.graphql.application.datamodel.request.Requester;
import co.jp.wever.graphql.application.datamodel.request.UpdateSectionInput;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.converter.article.ArticleDetailConverter;
import co.jp.wever.graphql.domain.converter.article.DraftArticleConverter;
import co.jp.wever.graphql.domain.converter.article.PublishArticleConverter;
import co.jp.wever.graphql.domain.domainmodel.article.ArticleDetail;
import co.jp.wever.graphql.domain.domainmodel.article.DraftArticle;
import co.jp.wever.graphql.domain.domainmodel.article.PublishArticle;
import co.jp.wever.graphql.domain.domainmodel.article.base.ArticleImageUrl;
import co.jp.wever.graphql.domain.domainmodel.article.base.ArticleTitle;
import co.jp.wever.graphql.domain.domainmodel.user.UserId;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import co.jp.wever.graphql.infrastructure.repository.article.FindArticleRepositoryImpl;
import co.jp.wever.graphql.infrastructure.repository.article.UpdateArticleRepositoryImpl;

@Service
public class UpdateArticleService {
    private final FindArticleRepositoryImpl findArticleRepository;
    private final UpdateArticleRepositoryImpl updateArticleRepository;

    public UpdateArticleService(
        FindArticleRepositoryImpl findArticleRepository, UpdateArticleRepositoryImpl updateArticleRepository) {
        this.findArticleRepository = findArticleRepository;
        this.updateArticleRepository = updateArticleRepository;
    }

    public void updateArticleTitle(String articleId, String userId, String title) {
        // TODO: ユーザー情報だけ取得したい
        // このクエリは重いので負荷がかかってしまうと思われる
        ArticleDetail articleDetail = ArticleDetailConverter.toArticleDetail(findArticleRepository.findOne(articleId));
        //        ArticleBase articleBase = ArticleBaseConverter.toArticleBase(articleInput);
        if (!articleDetail.canUpdate(UserId.of(userId))) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        ArticleTitle articleTitle = ArticleTitle.of(title);

        updateArticleRepository.updateTitle(articleId, articleTitle.getValue());
    }

    public void updateArticleImageUrl(String articleId, String userId, String imageUrl) {
        // TODO: ユーザー情報だけ取得したい
        // このクエリは重いので負荷がかかってしまうと思われる
        ArticleDetail articleDetail = ArticleDetailConverter.toArticleDetail(findArticleRepository.findOne(articleId));

        if (!articleDetail.canUpdate(UserId.of(userId))) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        ArticleImageUrl articleImageUrl = ArticleImageUrl.of(imageUrl);

        updateArticleRepository.updateImageUrl(articleId, articleImageUrl.getValue());
    }

    public void updateArticleTags(String articleId, String userId, List<String> tags) {
        // TODO: ユーザー情報だけ取得したい
        // このクエリは重いので負荷がかかってしまうと思われる
        ArticleDetail articleDetail = ArticleDetailConverter.toArticleDetail(findArticleRepository.findOne(articleId));

        // TODO: ドメインに移動させる
        if (tags.size() > 5) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_TAG_COUNT.getString());
        }

        if (!articleDetail.canUpdate(UserId.of(userId))) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        updateArticleRepository.updateTags(articleId, tags);
    }

    public void publishArticle(ArticleInput articleInput, List<UpdateSectionInput> sectionInputs, Requester requester) {
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
        UserId authorId = UserId.of(findArticleRepository.findAuthorId(articleInput.getId()));
        UserId requesterId = UserId.of(requester.getUserId());

        if (!authorId.same(requesterId)) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        DraftArticle draftArticle = DraftArticleConverter.toDraftArticle(articleInput, sectionInputs);

        updateArticleRepository.draftOne(draftArticle, authorId.getValue());
    }

    public void likeArticle(String articleId, String userId) {
        updateArticleRepository.likeOne(articleId, userId);
    }

    public void deleteLikeArticle(String articleId, String userId) {
        updateArticleRepository.deleteLikeOne(articleId, userId);
    }

    public void finishArticle(String articleId, String userId) {
        updateArticleRepository.finishOne(articleId, userId);
    }

    public void deleteFinishArticle(String articleId, String userId) {
        updateArticleRepository.deleteFinishOne(articleId, userId);
    }
}
