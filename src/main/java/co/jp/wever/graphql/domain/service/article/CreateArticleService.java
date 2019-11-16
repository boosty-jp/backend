package co.jp.wever.graphql.domain.service.article;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import co.jp.wever.graphql.application.datamodel.request.Requester;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import co.jp.wever.graphql.infrastructure.repository.article.CreateArticleRepositoryImpl;

@Service
public class CreateArticleService {
    private final CreateArticleRepositoryImpl createArticleRepository;

    public CreateArticleService(CreateArticleRepositoryImpl createArticleRepository) {
        this.createArticleRepository = createArticleRepository;
    }

    public String initArticle(Requester requester) {
        if(requester.isGuest()){
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }
        return createArticleRepository.initOne(requester.getUserId());
    }
}
