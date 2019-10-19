package co.jp.wever.graphql.domain.repository.article;

import org.springframework.stereotype.Repository;

import co.jp.wever.graphql.infrastructure.datamodel.article.ArticleBaseEntity;

@Repository
public interface CreateArticleRepository {
    String createOne(ArticleBaseEntity articleBaseEntity, String userId);
}
