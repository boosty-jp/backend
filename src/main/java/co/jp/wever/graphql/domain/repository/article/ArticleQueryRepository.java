package co.jp.wever.graphql.domain.repository.article;

import org.springframework.stereotype.Repository;

import java.util.List;

import co.jp.wever.graphql.domain.domainmodel.search.SearchCondition;
import co.jp.wever.graphql.infrastructure.datamodel.article.ArticleEntity;

@Repository
public interface ArticleQueryRepository {
    ArticleEntity findOne(String articleId, String userId);

    ArticleEntity findOneForGuest(String articleId);

    List<ArticleEntity> findCreated(String userId, SearchCondition searchCondition);

    List<ArticleEntity> findCreatedBySelf(String userId, SearchCondition searchCondition);

    List<ArticleEntity> findActioned(String userId, SearchCondition searchCondition);

    List<ArticleEntity> findFamous();

    String findAuthorId(String articleId);

    int publishedArticleCount(List<String> articleIds);
}
