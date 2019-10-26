package co.jp.wever.graphql.domain.service.article;

import org.springframework.stereotype.Service;

import co.jp.wever.graphql.infrastructure.repository.article.CreateArticleRepositoryImpl;

@Service
public class CreateArticleService {
    private final CreateArticleRepositoryImpl createArticleRepository;

    public CreateArticleService(CreateArticleRepositoryImpl createArticleRepository) {
        this.createArticleRepository = createArticleRepository;
    }

    public String initArticle(String userId) {

//        if(!userId.isEmpty()){
//            throw new GraphQLCustomException(400, "A custom error message");
//        }
        return createArticleRepository.initOne(userId);
    }
}
