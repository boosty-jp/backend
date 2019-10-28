package co.jp.wever.graphql.domain.service.article;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.converter.article.ArticleDetailConverter;
import co.jp.wever.graphql.domain.domainmodel.article.ArticleDetail;
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
        //TODO: 詳細とらずにステータスと著者だけ取るようにしてもいいかも
        ArticleDetail articleDetail = ArticleDetailConverter.toArticleDetail(findArticleRepository.findOne(articleId));

        if (!articleDetail.canDelete(UserId.of(userId))) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        deleteArticleRepository.deleteOne(articleId, userId);
    }
}
