package co.jp.wever.graphql.domain.repository.article;

import org.springframework.stereotype.Repository;

import java.util.List;

import co.jp.wever.graphql.infrastructure.datamodel.article.ArticleBaseEntity;

@Repository
public interface CreateArticleRepository {
    String createOne(ArticleBaseEntity articleBaseEntity, List<String> tagIds, String userId);
}
