package co.jp.wever.graphql.domain.service.datafetchers;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.application.converter.search.SearchConditionConverter;
import co.jp.wever.graphql.application.converter.article.ArticleInputConverter;
import co.jp.wever.graphql.application.converter.article.ArticleResponseConverter;
import co.jp.wever.graphql.application.converter.requester.RequesterConverter;
import co.jp.wever.graphql.application.datamodel.request.article.ArticleInput;
import co.jp.wever.graphql.application.datamodel.request.user.Requester;
import co.jp.wever.graphql.application.datamodel.request.search.SearchConditionInput;
import co.jp.wever.graphql.application.datamodel.response.mutation.CreateResponse;
import co.jp.wever.graphql.application.datamodel.response.query.article.ArticleListResponse;
import co.jp.wever.graphql.domain.service.article.ArticleMutationService;
import co.jp.wever.graphql.domain.service.article.ArticleQueryService;
import co.jp.wever.graphql.infrastructure.datamodel.article.ArticleEntity;
import co.jp.wever.graphql.infrastructure.datamodel.article.ArticleListEntity;
import graphql.schema.DataFetcher;

@Component
public class ArticleDataFetcher {

    private final ArticleQueryService articleQueryService;
    private final ArticleMutationService articleMutationService;
    private final RequesterConverter requesterConverter;

    public ArticleDataFetcher(
        ArticleQueryService articleQueryService,
        ArticleMutationService articleMutationService,
        RequesterConverter requesterConverter) {
        this.articleQueryService = articleQueryService;
        this.articleMutationService = articleMutationService;
        this.requesterConverter = requesterConverter;
    }

    ///////////////////////////////
    //////////// Query ////////////
    ///////////////////////////////

    public DataFetcher articleDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String articleId = dataFetchingEnvironment.getArgument("articleId");

            return ArticleResponseConverter.toArticleResponse(articleQueryService.findArticle(articleId, requester));
        };
    }

    public DataFetcher createdArticlesDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            SearchConditionInput searchConditionInput =
                SearchConditionConverter.toSearchCondition(dataFetchingEnvironment);

            ArticleListEntity result = articleQueryService.findCreatedArticles(userId, searchConditionInput);

            return ArticleListResponse.builder()
                                      .articles(result.getArticles()
                                                      .stream()
                                                      .map(r -> ArticleResponseConverter.toArticleResponseForList(r))
                                                      .collect(Collectors.toList()))
                                      .sumCount(result.getSumCount());
        };
    }

    public DataFetcher createdArticlesBySelfDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            SearchConditionInput searchConditionInput =
                SearchConditionConverter.toSearchCondition(dataFetchingEnvironment);

            ArticleListEntity result = articleQueryService.findCreatedArticlesBySelf(requester, searchConditionInput);

            return ArticleListResponse.builder()
                                      .articles(result.getArticles()
                                                      .stream()
                                                      .map(r -> ArticleResponseConverter.toArticleResponseForList(r))
                                                      .collect(Collectors.toList()))
                                      .sumCount(result.getSumCount());
        };
    }

    public DataFetcher actionedArticlesDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            SearchConditionInput searchConditionInput =
                SearchConditionConverter.toSearchCondition(dataFetchingEnvironment);

            ArticleListEntity result = articleQueryService.findActionedArticles(userId, searchConditionInput);

            return ArticleListResponse.builder()
                                      .articles(result.getArticles()
                                                      .stream()
                                                      .map(r -> ArticleResponseConverter.toArticleResponseForList(r))
                                                      .collect(Collectors.toList()))
                                      .sumCount(result.getSumCount());
        };
    }

    public DataFetcher actionedArticlesBySelfDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            SearchConditionInput searchConditionInput =
                SearchConditionConverter.toSearchCondition(dataFetchingEnvironment);

            ArticleListEntity result = articleQueryService.findActionedArticlesBySelf(requester, searchConditionInput);

            return ArticleListResponse.builder()
                                      .articles(result.getArticles()
                                                      .stream()
                                                      .map(r -> ArticleResponseConverter.toArticleResponseForList(r))
                                                      .collect(Collectors.toList()))
                                      .sumCount(result.getSumCount());
        };
    }

    public DataFetcher famousArticlesDataFetcher() {
        return dataFetchingEnvironment -> {
            List<ArticleEntity> results = articleQueryService.findFamousArticle();
            return results.stream()
                          .map(r -> ArticleResponseConverter.toArticleResponseForList(r))
                          .collect(Collectors.toList());
        };
    }

    ///////////////////////////////
    ///////// Mutations   /////////
    ///////////////////////////////
    public DataFetcher deleteArticleDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String articleId = dataFetchingEnvironment.getArgument("articleId");
            articleMutationService.delete(articleId, requester);

            return true;
        };
    }

    public DataFetcher publishArticleDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            ArticleInput articleInput = ArticleInputConverter.toArticleInput(dataFetchingEnvironment);
            String articleId = articleMutationService.publish(articleInput, requester);

            return CreateResponse.builder().id(articleId).build();
        };
    }

    public DataFetcher draftArticleDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            ArticleInput articleInput = ArticleInputConverter.toArticleInput(dataFetchingEnvironment);
            String articleId = articleMutationService.draft(articleInput, requester);

            return CreateResponse.builder().id(articleId).build();
        };
    }

    public DataFetcher likeArticleDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String articleId = dataFetchingEnvironment.getArgument("articleId");
            articleMutationService.like(articleId, requester);

            return true;
        };
    }

    public DataFetcher clearLikeArticleDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String articleId = dataFetchingEnvironment.getArgument("articleId");
            articleMutationService.clearLike(articleId, requester);

            return true;
        };
    }

    public DataFetcher learnArticleDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String articleId = dataFetchingEnvironment.getArgument("articleId");
            articleMutationService.learn(articleId, requester);

            return true;
        };
    }

    public DataFetcher clearLearnArticleDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String articleId = dataFetchingEnvironment.getArgument("articleId");
            articleMutationService.clearLearn(articleId, requester);

            return true;
        };
    }
}
