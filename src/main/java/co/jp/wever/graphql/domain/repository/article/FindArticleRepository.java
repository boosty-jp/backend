package co.jp.wever.graphql.domain.repository.article;

import org.springframework.stereotype.Repository;

import java.util.List;

import co.jp.wever.graphql.infrastructure.datamodel.article.aggregation.ArticleDetailEntity;
import co.jp.wever.graphql.infrastructure.datamodel.article.aggregation.ArticleOutlineEntity;

@Repository
public interface FindArticleRepository {
    ArticleDetailEntity findOne(String articleId);

    List<ArticleOutlineEntity> findAll(String userId);

    List<ArticleOutlineEntity> findAllPublished(String userId);

    List<ArticleOutlineEntity> findAllDrafted(String userId);

    List<ArticleOutlineEntity> findAllLiked(String userId);

    List<ArticleOutlineEntity> findAllLearned(String userId);

    List<ArticleOutlineEntity> findAllBookmarked(String userId);

    List<ArticleOutlineEntity> findFamous();

    List<ArticleOutlineEntity> findRelated(String userId);

}
