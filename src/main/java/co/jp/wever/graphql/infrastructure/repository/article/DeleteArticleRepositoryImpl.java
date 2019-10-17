package co.jp.wever.graphql.infrastructure.repository.article;

import org.springframework.stereotype.Component;

import co.jp.wever.graphql.domain.repository.article.DeleteArticleRepository;

@Component
public class DeleteArticleRepositoryImpl implements DeleteArticleRepository {
    public void deleteOne(String articleId, String userId) {
    }
}
