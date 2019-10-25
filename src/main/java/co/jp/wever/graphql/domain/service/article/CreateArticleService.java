package co.jp.wever.graphql.domain.service.article;

import org.springframework.stereotype.Service;

import co.jp.wever.graphql.application.datamodel.request.ArticleInput;
import co.jp.wever.graphql.domain.converter.article.ArticleBaseConverter;
import co.jp.wever.graphql.domain.domainmodel.article.base.ArticleBase;
import co.jp.wever.graphql.infrastructure.converter.entity.article.ArticleBaseEntityConverter;
import co.jp.wever.graphql.infrastructure.repository.article.CreateArticleRepositoryImpl;

@Service
public class CreateArticleService {
    private final CreateArticleRepositoryImpl createArticleRepository;

    public CreateArticleService(CreateArticleRepositoryImpl createArticleRepository) {
        this.createArticleRepository = createArticleRepository;
    }

    public String initArticle(String userId) {

        return createArticleRepository.initOne(userId);
    }
}
