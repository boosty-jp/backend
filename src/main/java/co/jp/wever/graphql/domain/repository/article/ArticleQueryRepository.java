package co.jp.wever.graphql.domain.repository.article;

import org.springframework.stereotype.Repository;

import java.util.List;

import co.jp.wever.graphql.domain.domainmodel.search.SearchCondition;
import co.jp.wever.graphql.infrastructure.datamodel.article.ArticleEntity;
import co.jp.wever.graphql.infrastructure.datamodel.article.aggregation.ArticleDetailEntity;

@Repository
public interface ArticleQueryRepository {
    ArticleEntity findOne(String articleId, String userId);

    ArticleEntity findOneForGuest(String articleId);

    List<ArticleEntity> findCreated(String userId, SearchCondition searchCondition);

    List<ArticleEntity> findCreatedBySelf(String userId, SearchCondition searchCondition);

    List<ArticleEntity> findActioned(String userId, SearchCondition searchCondition);

    List<ArticleEntity> findFamous();

    List<ArticleDetailEntity> findRelated(String userId);

    String findAuthorId(String articleId);
}
