package co.jp.wever.graphql.infrastructure.repository.article;

import org.springframework.stereotype.Component;

import co.jp.wever.graphql.domain.repository.article.CreateArticleRepository;
import co.jp.wever.graphql.infrastructure.datamodel.article.ArticleBaseEntity;

@Component
public class CreateArticleRepositoryImpl implements CreateArticleRepository {

    @Override
    public String createOne(ArticleBaseEntity articleBaseEntity, String userId) {
        return "";
    }
}
