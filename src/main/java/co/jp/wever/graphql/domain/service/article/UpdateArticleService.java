package co.jp.wever.graphql.domain.service.article;

import org.springframework.stereotype.Service;

import java.util.List;

import co.jp.wever.graphql.domain.converter.article.ArticleDetailConverter;
import co.jp.wever.graphql.domain.domainmodel.article.ArticleDetail;
import co.jp.wever.graphql.domain.domainmodel.article.base.ArticleImageUrl;
import co.jp.wever.graphql.domain.domainmodel.article.base.Articletitle;
import co.jp.wever.graphql.domain.domainmodel.user.UserId;
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

    //    public void updateArticle(String articleId, String userId, ArticleInput articleInput)
    //        throws IllegalAccessException {
    //        // TODO: ユーザー情報だけ取得したい
    //        // このクエリは重いので負荷がかかってしまうと思われる
    //        ArticleDetail articleDetail = ArticleDetailConverter.toArticleDetail(findArticleRepository.findOne(articleId));
    //        //        ArticleBase articleBase = ArticleBaseConverter.toArticleBase(articleInput);
    //        if (!articleDetail.canUpdate(UserId.of(userId))) {
    //            throw new IllegalAccessException();
    //        }
    //
    //        updateArticleRepository.updateOne(ArticleBaseEntityConverter.toArticleBaseEntity(articleId,
    //                                                                                         ArticleBaseConverter.toArticleBase(
    //                                                                                             articleInput)),
    //                                          articleInput.getTags());
    //    }

    public void updateArticleTitle(String articleId, String userId, String title) throws IllegalAccessException {
        // TODO: ユーザー情報だけ取得したい
        // このクエリは重いので負荷がかかってしまうと思われる
        ArticleDetail articleDetail = ArticleDetailConverter.toArticleDetail(findArticleRepository.findOne(articleId));
        //        ArticleBase articleBase = ArticleBaseConverter.toArticleBase(articleInput);
        if (!articleDetail.canUpdate(UserId.of(userId))) {
            throw new IllegalAccessException();
        }

        Articletitle articleTitle = Articletitle.of(title);

        updateArticleRepository.updateTitle(articleId, articleTitle.getValue());
    }

    public void updateArticleImageUrl(String articleId, String userId, String imageUrl) throws IllegalAccessException {
        // TODO: ユーザー情報だけ取得したい
        // このクエリは重いので負荷がかかってしまうと思われる
        ArticleDetail articleDetail = ArticleDetailConverter.toArticleDetail(findArticleRepository.findOne(articleId));

        if (!articleDetail.canUpdate(UserId.of(userId))) {
            throw new IllegalAccessException();
        }

        ArticleImageUrl articleImageUrl = ArticleImageUrl.of(imageUrl);

        updateArticleRepository.updateImageUrl(articleId, articleImageUrl.getValue());
    }

    public void updateArticleTags(String articleId, String userId, List<String> tags) throws IllegalAccessException {
        // TODO: ユーザー情報だけ取得したい
        // このクエリは重いので負荷がかかってしまうと思われる
        ArticleDetail articleDetail = ArticleDetailConverter.toArticleDetail(findArticleRepository.findOne(articleId));

        if (!articleDetail.canUpdate(UserId.of(userId))) {
            throw new IllegalAccessException();
        }

        updateArticleRepository.updateTags(articleId, tags);
    }

    public void publishArticle(String articleId, String userId) throws IllegalAccessException {
        // TODO: ユーザー情報だけ取得したい
        // このクエリは重いので負荷がかかってしまうと思われる
        ArticleDetail articleDetail = ArticleDetailConverter.toArticleDetail(findArticleRepository.findOne(articleId));

        //        ArticleBase articleBase = ArticleBaseConverter.toArticleBase(articleInput);
        if (!articleDetail.canPublish(UserId.of(userId))) {
            throw new IllegalAccessException();
        }

        updateArticleRepository.publishOne(articleId, userId);
    }

    public void draftArticle(String articleId, String userId) throws IllegalAccessException {
        // TODO: ユーザー情報だけ取得したい
        // このクエリは重いので負荷がかかってしまうと思われる
        ArticleDetail articleDetail = ArticleDetailConverter.toArticleDetail(findArticleRepository.findOne(articleId));

        if (!articleDetail.canDraft(UserId.of(userId))) {
            throw new IllegalAccessException();
        }

        updateArticleRepository.draftOne(articleId, userId);
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
