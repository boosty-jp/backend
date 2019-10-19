package co.jp.wever.graphql.infrastructure.repository.article;

import org.springframework.stereotype.Component;

import java.util.List;

import co.jp.wever.graphql.domain.repository.article.FindArticleRepository;
import co.jp.wever.graphql.infrastructure.datamodel.article.aggregation.ArticleDetailEntity;
import co.jp.wever.graphql.infrastructure.datamodel.article.aggregation.ArticleOutlineEntity;

@Component
public class FindArticleRepositoryImpl implements FindArticleRepository {
    @Override
    public ArticleDetailEntity findOne(String articleId) {
        return ArticleDetailEntity.builder().build();
    }

    @Override
    public List<ArticleOutlineEntity> findAll(String userId) {
        return null;
    }

    @Override
    public List<ArticleOutlineEntity> findAllPublished(String userId) {
        return null;
    }

    @Override
    public List<ArticleOutlineEntity> findAllDrafted(String userId) {
        return null;
    }

    @Override
    public List<ArticleOutlineEntity> findAllLiked(String userId) {
        return null;
    }

    @Override
    public List<ArticleOutlineEntity> findAllLearned(String userId) {
        return null;
    }

    @Override
    public List<ArticleOutlineEntity> findAllBookmarked(String userId) {
        return null;
    }

    @Override
    public List<ArticleOutlineEntity> findFamous() {
        return null;
    }

    public List<ArticleOutlineEntity> findRelated(String userId) {
        return null;
    }
}
