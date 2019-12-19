package co.jp.wever.graphql.domain.repository.article;

import org.springframework.stereotype.Repository;

import co.jp.wever.graphql.domain.domainmodel.article.Article;

@Repository
public interface ArticleMutationRepository {
    void publish(String articleId , Article article, String userId);

    String publishByEntry(Article article, String userId);

    void draft(String articleId , Article article, String userId);

    String draftByEntry(Article article, String userId);

    void delete(String articleId, String userId);

    void like(String articleId, String userId);

    void clearLike(String articleId, String userId);

    void learn(String articleId, String userId);

    void clearLearn(String articleId, String userId);

}
