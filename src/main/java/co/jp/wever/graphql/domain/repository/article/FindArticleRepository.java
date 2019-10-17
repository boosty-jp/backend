package co.jp.wever.graphql.domain.repository.article;

import org.springframework.stereotype.Repository;

import java.util.List;

import co.jp.wever.graphql.infrastructure.datamodel.Article;
import co.jp.wever.graphql.infrastructure.datamodel.article.aggregation.ArticleDetailEntity;
import co.jp.wever.graphql.infrastructure.datamodel.article.aggregation.ArticleEntity;

@Repository
public interface FindArticleRepository {
    ArticleDetailEntity findOne(String articleId);

    List<ArticleEntity> findAll(String userId);

    List<ArticleEntity> findAllPublished(String userId);

    List<ArticleEntity> findAllDrafted(String userId);

    List<ArticleEntity> findAllLiked(String userId);

    List<ArticleEntity> findAllLearned(String userId);

    List<ArticleEntity> findAllBookmarked(String userId);

    List<ArticleEntity> findFamous();

    List<ArticleEntity> findRelated(String userId);

}
