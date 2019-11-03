package co.jp.wever.graphql.domain.service.article;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.domainmodel.user.UserId;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import co.jp.wever.graphql.infrastructure.repository.article.DeleteArticleRepositoryImpl;
import co.jp.wever.graphql.infrastructure.repository.article.FindArticleRepositoryImpl;

@Service
public class DeleteArticleService {

    private final FindArticleRepositoryImpl findArticleRepository;
    private final DeleteArticleRepositoryImpl deleteArticleRepository;

    public DeleteArticleService(
        FindArticleRepositoryImpl findArticleRepository, DeleteArticleRepositoryImpl deleteArticleRepository) {
        this.findArticleRepository = findArticleRepository;
        this.deleteArticleRepository = deleteArticleRepository;
    }

    public void deleteArticle(String articleId, String userId) {
        UserId authorId = UserId.of(findArticleRepository.findAuthorId(articleId));
        UserId deleterId = UserId.of(userId);

        if (authorId.same(deleterId)) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        deleteArticleRepository.deleteOne(articleId, userId);
    }
}
