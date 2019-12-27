package co.jp.wever.graphql.domain.repository.article;

import org.springframework.stereotype.Repository;

import java.util.List;

import co.jp.wever.graphql.domain.domainmodel.search.SearchCondition;
import co.jp.wever.graphql.infrastructure.datamodel.article.ArticleEntity;
import co.jp.wever.graphql.infrastructure.datamodel.article.ArticleListEntity;

@Repository
public interface ArticleQueryRepository {
    ArticleEntity findOne(String articleId, String userId);

    ArticleEntity findOneForGuest(String articleId);

    ArticleListEntity findCreated(String userId, SearchCondition searchCondition);

    ArticleListEntity findCreatedBySelf(String userId, SearchCondition searchCondition);

    ArticleListEntity findActioned(String userId, SearchCondition searchCondition);

    List<ArticleEntity> findFamous();

    String findAuthorId(String articleId);

    int publishedArticleCount(List<String> articleIds);
}
