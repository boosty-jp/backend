package co.jp.wever.graphql.infrastructure.repository.article;

import org.springframework.stereotype.Component;

import java.util.List;

import co.jp.wever.graphql.domain.repository.article.FindArticleRepository;
import co.jp.wever.graphql.infrastructure.datamodel.article.aggregation.ArticleDetailEntity;
import co.jp.wever.graphql.infrastructure.datamodel.article.aggregation.ArticleEntity;

@Component
public class FindArticleRepositoryImpl implements FindArticleRepository {
    @Override
    public ArticleDetailEntity findOne(String articleId) {
        return ArticleDetailEntity.builder().build();
    }

    @Override
    public List<ArticleEntity> findAll(String userId) {
        return null;
    }

    @Override
    public List<ArticleEntity> findAllPublished(String userId) {
        return null;
    }

    @Override
    public List<ArticleEntity> findAllDrafted(String userId) {
        return null;
    }

    @Override
    public List<ArticleEntity> findAllLiked(String userId) {
        return null;
    }

    @Override
    public List<ArticleEntity> findAllLearned(String userId) {
        return null;
    }

    @Override
    public List<ArticleEntity> findAllBookmarked(String userId) {
        return null;
    }

    @Override
    public List<ArticleEntity> findFamous() {
        return null;
    }

    public List<ArticleEntity> findRelated(String userId) {
        return null;
    }
}
