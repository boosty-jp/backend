package co.jp.wever.graphql.infrastructure.repository.article;

import org.springframework.stereotype.Component;

import co.jp.wever.graphql.domain.repository.article.UpdateArticleRepository;
import co.jp.wever.graphql.infrastructure.datamodel.article.ArticleBaseEntity;

@Component
public class UpdateArticleRepositoryImpl implements UpdateArticleRepository {

    @Override
    public void updateOne(String userId, ArticleBaseEntity targetArticle) {

    }

    @Override
    public void publishOne(String articleId, String userId) {

    }

    @Override
    public void draftOne(String articleId, String userId) {

    }

    @Override
    public void likeOne(String articleId, String userId) {
    }

    @Override
    public void finishOne(String articleId, String userId) {
    }
}
