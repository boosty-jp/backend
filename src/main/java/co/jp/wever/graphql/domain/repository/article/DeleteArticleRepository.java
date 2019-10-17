package co.jp.wever.graphql.domain.repository.article;

import org.springframework.stereotype.Repository;

@Repository
public interface DeleteArticleRepository {
    void deleteOne(String articleId, String userId);
}
