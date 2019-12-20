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
import co.jp.wever.graphql.infrastructure.repository.user.UserQueryRepositoryImpl;

@Service
public class ArticleQueryService {

    private final ArticleQueryRepositoryImpl articleQueryRepository;
    private final UserQueryRepositoryImpl userQueryRepository;

    public ArticleQueryService(
        ArticleQueryRepositoryImpl articleQueryRepository, UserQueryRepositoryImpl userQueryRepository) {
        this.articleQueryRepository = articleQueryRepository;
        this.userQueryRepository = userQueryRepository;
    }

    public ArticleEntity findArticle(String articleId, Requester requester) {
        ArticleEntity articleEntity;
        if (requester.isGuest()) {
            articleEntity = articleQueryRepository.findOneForGuest(articleId);
        } else {
            articleEntity = articleQueryRepository.findOne(articleId, requester.getUserId());
        }

        if (!canRead(articleEntity, requester.getUserId())) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        return articleEntity;
    }

    public List<ArticleEntity> findCreatedArticles(String userId, SearchConditionInput searchConditionInput) {
        SearchCondition searchCondition = SearchConditionFactory.makeFilteredPublished(searchConditionInput);

        return articleQueryRepository.findCreated(userId, searchCondition);
    }

    public List<ArticleEntity> findCreatedArticlesBySelf(
        Requester requester, SearchConditionInput searchConditionInput) {
        SearchCondition searchCondition = SearchConditionFactory.make(searchConditionInput);

        if (!searchCondition.createdFilter()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_SEARCH_CONDITION.getString());
        }

        return articleQueryRepository.findCreatedBySelf(requester.getUserId(), searchCondition);
    }

    public List<ArticleEntity> findActionedArticles(String userId, SearchConditionInput searchConditionInput) {
        SearchCondition searchCondition = SearchConditionFactory.make(searchConditionInput);

        if (!searchCondition.validActionedFilter()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_SEARCH_CONDITION.getString());
        }

        UserEntity userEntity = userQueryRepository.findOne(userId);

        if (!searchCondition.canSearch(userEntity.getLikePublic(), userEntity.getLearnPublic())) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.SEARCH_FORBIDDEN.getString());
        }

        return articleQueryRepository.findActioned(userId, searchCondition);
    }

    public List<ArticleEntity> findActionedArticlesBySelf(
        Requester requester, SearchConditionInput searchConditionInput) {
        SearchCondition searchCondition = SearchConditionFactory.make(searchConditionInput);

        if (!searchCondition.validActionedFilter()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_SEARCH_CONDITION.getString());
        }

        return articleQueryRepository.findActioned(requester.getUserId(), searchCondition);
    }

    public List<ArticleEntity> findFamousArticle() {
        return articleQueryRepository.findFamous();
    }

    //TODO: ドメイン化
    private boolean canRead(ArticleEntity articleEntity, String requesterId) {
        if (articleEntity.getBase().getStatus().equals(EdgeLabel.PUBLISH.getString())) {
            return true;
        }

        return requesterId.equals(articleEntity.getAuthor().getUserId());
    }
}
