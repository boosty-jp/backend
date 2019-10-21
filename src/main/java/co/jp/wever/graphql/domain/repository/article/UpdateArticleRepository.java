package co.jp.wever.graphql.domain.repository.article;

import org.springframework.stereotype.Repository;

import java.util.List;

import co.jp.wever.graphql.infrastructure.datamodel.article.ArticleBaseEntity;

@Repository
public interface UpdateArticleRepository {
    void updateOne(ArticleBaseEntity base, List<String> tagIds);

    void publishOne(String articleId, String userId);

    void draftOne(String articleId, String userId);

    void likeOne(String articleId, String userId);

    void finishOne(String articleId, String userId);
}
