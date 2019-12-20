package co.jp.wever.graphql.domain.service.article;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import co.jp.wever.graphql.application.datamodel.request.Requester;
import co.jp.wever.graphql.application.datamodel.request.SearchConditionInput;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.factory.SearchConditionFactory;
import co.jp.wever.graphql.domain.domainmodel.search.SearchCondition;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import co.jp.wever.graphql.infrastructure.constant.edge.EdgeLabel;
import co.jp.wever.graphql.infrastructure.datamodel.article.ArticleEntity;
import co.jp.wever.graphql.infrastructure.datamodel.user.UserEntity;
import co.jp.wever.graphql.infrastructure.repository.article.ArticleQueryRepositoryImpl;
import co.jp.wever.graphql.infrastructure.repository.user.FindUserRepositoryImpl;

@Service
public class ArticleQueryService {

    private final ArticleQueryRepositoryImpl findArticleRepository;
    private final FindUserRepositoryImpl findUserRepository;

    ArticleQueryService(
        ArticleQueryRepositoryImpl findArticleRepository, FindUserRepositoryImpl findUserRepository) {
        this.findArticleRepository = findArticleRepository;
        this.findUserRepository = findUserRepository;
    }

    public ArticleEntity findArticle(String articleId, Requester requester) {
        ArticleEntity articleEntity;
        if (requester.isGuest()) {
            articleEntity = findArticleRepository.findOneForGuest(articleId);
        } else {
            articleEntity = findArticleRepository.findOne(articleId, requester.getUserId());
        }

        if (!canRead(articleEntity, requester.getUserId())) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        return articleEntity;
    }

    public List<ArticleEntity> findCreatedArticles(String userId, SearchConditionInput searchConditionInput) {
        SearchCondition searchCondition = SearchConditionFactory.makeFilteredPublished(searchConditionInput);

        return findArticleRepository.findCreated(userId, searchCondition);
    }

    public List<ArticleEntity> findCreatedArticlesBySelf(
        Requester requester, SearchConditionInput searchConditionInput) {
        SearchCondition searchCondition = SearchConditionFactory.make(searchConditionInput);

        if (!searchCondition.createdFilter()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_SEARCH_CONDITION.getString());
        }

        return findArticleRepository.findCreatedBySelf(requester.getUserId(), searchCondition);
    }

    public List<ArticleEntity> findActionedArticles(String userId, SearchConditionInput searchConditionInput) {
        SearchCondition searchCondition = SearchConditionFactory.make(searchConditionInput);

        if (!searchCondition.validActionedFilter()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_SEARCH_CONDITION.getString());
        }

        UserEntity userEntity = this.findUserRepository.findOne(userId);

        if (!searchCondition.canSearch(userEntity.getLikePublic(), userEntity.getLearnPublic())) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.SEARCH_FORBIDDEN.getString());
        }

        return findArticleRepository.findActioned(userId, searchCondition);
    }

    public List<ArticleEntity> findActionedArticlesBySelf(
        Requester requester, SearchConditionInput searchConditionInput) {
        SearchCondition searchCondition = SearchConditionFactory.make(searchConditionInput);

        if (!searchCondition.validActionedFilter()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_SEARCH_CONDITION.getString());
        }

        return findArticleRepository.findActioned(requester.getUserId(), searchCondition);
    }

    public List<ArticleEntity> findFamousArticle() {
        return findArticleRepository.findFamous();
    }

    private boolean canRead(ArticleEntity articleEntity, String requesterId) {
        if (articleEntity.getBase().getStatus().equals(EdgeLabel.PUBLISH.getString())) {
            return true;
        }

        return requesterId.equals(articleEntity.getAuthor().getUserId());
    }
}
