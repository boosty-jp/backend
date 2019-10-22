package co.jp.wever.graphql.domain.repository.article;

import org.springframework.stereotype.Repository;

import java.util.List;

import co.jp.wever.graphql.infrastructure.datamodel.article.aggregation.ArticleDetailEntity;

@Repository
public interface FindArticleRepository {
    ArticleDetailEntity findOne(String articleId);

    List<ArticleDetailEntity> findAll(String userId);

    List<ArticleDetailEntity> findAllPublished(String userId);

    List<ArticleDetailEntity> findAllDrafted(String userId);

    List<ArticleDetailEntity> findAllLiked(String userId);

    List<ArticleDetailEntity> findAllLearned(String userId);

    List<ArticleDetailEntity> findAllBookmarked(String userId);

    List<ArticleDetailEntity> findFamous();

    List<ArticleDetailEntity> findRelated(String userId);

}
