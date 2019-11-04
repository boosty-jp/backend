package co.jp.wever.graphql.domain.repository.article;

import org.springframework.stereotype.Repository;

import java.util.List;

import co.jp.wever.graphql.domain.domainmodel.article.DraftArticle;
import co.jp.wever.graphql.domain.domainmodel.article.PublishArticle;
import co.jp.wever.graphql.infrastructure.datamodel.article.ArticleBaseEntity;

@Repository
public interface UpdateArticleRepository {
//    void updateOne(ArticleBaseEntity base, List<String> tagIds);

    void updateTitle(String articleId, String title);

    void updateImageUrl(String articleId, String imageUrl);

    void updateTags(String articleId, List<String> tagIds);

    void publishOne(PublishArticle publishArticle, String userId);

    void draftOne(DraftArticle draftArticle, String userId);

    void likeOne(String articleId, String userId);

    void deleteLikeOne(String articleId, String userId);

    void finishOne(String articleId, String userId);

    void deleteFinishOne(String articleId, String userId);
}
