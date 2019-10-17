package co.jp.wever.graphql.domain.repository.article;

import org.springframework.stereotype.Repository;

import co.jp.wever.graphql.infrastructure.datamodel.article.ArticleBaseEntity;

@Repository
public interface UpdateArticleRepository {
    void updateOne(String userId, ArticleBaseEntity base);

    void publishOne(String articleId, String userId);

    void draftOne(String articleId, String userId);

    void bookmarkOne(String articleId, String userId);

    void likeOne(String articleId, String userId);
}
