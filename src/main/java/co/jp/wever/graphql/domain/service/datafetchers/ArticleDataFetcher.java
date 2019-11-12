package co.jp.wever.graphql.domain.service.datafetchers;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.application.converter.article.ArticleDetailResponseConverter;
import co.jp.wever.graphql.application.converter.article.ArticleInputConverter;
import co.jp.wever.graphql.application.converter.requester.RequesterConverter;
import co.jp.wever.graphql.application.converter.section.UpdateSectionInputsConverter;
import co.jp.wever.graphql.application.datamodel.request.ArticleInput;
import co.jp.wever.graphql.application.datamodel.request.Requester;
import co.jp.wever.graphql.application.datamodel.request.UpdateSectionInput;
import co.jp.wever.graphql.application.datamodel.response.mutation.UpdateImageResponse;
import co.jp.wever.graphql.domain.domainmodel.TokenVerifier;
import co.jp.wever.graphql.domain.service.article.CreateArticleService;
import co.jp.wever.graphql.domain.service.article.DeleteArticleService;
import co.jp.wever.graphql.domain.service.article.FindArticleService;
import co.jp.wever.graphql.domain.service.article.UpdateArticleService;
import co.jp.wever.graphql.application.datamodel.response.mutation.CreateResponse;
import co.jp.wever.graphql.application.datamodel.response.mutation.UpdateResponse;
import graphql.schema.DataFetcher;

@Component
public class ArticleDataFetcher {

    private final TokenVerifier tokenVerifier;
    private final FindArticleService findArticleService;
    private final CreateArticleService createArticleService;
    private final UpdateArticleService updateArticleService;
    private final DeleteArticleService deleteArticleService;
    private final RequesterConverter requesterConverter;

    public ArticleDataFetcher(
        TokenVerifier tokenVerifier,
        FindArticleService findArticleService,
        CreateArticleService createArticleService,
        UpdateArticleService updateArticleService,
        DeleteArticleService deleteArticleService,
        RequesterConverter requesterConverter) {
        this.tokenVerifier = tokenVerifier;
        this.findArticleService = findArticleService;
        this.createArticleService = createArticleService;
        this.updateArticleService = updateArticleService;
        this.deleteArticleService = deleteArticleService;
        this.requesterConverter = requesterConverter;
    }

    ///////////////////////////////
    //////////// Query ////////////
    ///////////////////////////////

    public DataFetcher articleDataFetcher() {
        return dataFetchingEnvironment -> {
            String token = (String) dataFetchingEnvironment.getContext();
            String userId = tokenVerifier.getUserId(token);

            String articleId = dataFetchingEnvironment.getArgument("articleId");

            return ArticleDetailResponseConverter.toArticleDetailResponse(findArticleService.findArticleDetail(articleId,
                                                                                                               userId));
        };
    }

    public DataFetcher allArticlesDataFetcher() {
        return dataFetchingEnvironment -> {
            String token = (String) dataFetchingEnvironment.getContext();
            String userId = tokenVerifier.getUserId(token);
            return findArticleService.findAllArticle(userId)
                                     .stream()
                                     .map(a -> ArticleDetailResponseConverter.toArticleDetailResponse(a))
                                     .collect(Collectors.toList());
        };
    }

    public DataFetcher allPublishedArticlesDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            return findArticleService.findAllPublishedArticle(userId)
                                     .stream()
                                     .map(a -> ArticleDetailResponseConverter.toArticleDetailResponse(a))
                                     .collect(Collectors.toList());
        };
    }

    public DataFetcher allDraftedArticlesDataFetcher() {
        return dataFetchingEnvironment -> {
            String token = (String) dataFetchingEnvironment.getContext();
            String userId = tokenVerifier.getUserId(token);
            return findArticleService.findAllDraftedArticle(userId)
                                     .stream()
                                     .map(a -> ArticleDetailResponseConverter.toArticleDetailResponse(a))
                                     .collect(Collectors.toList());
        };
    }

    public DataFetcher allLikedArticlesDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            return findArticleService.findAllLikedArticle(userId)
                                     .stream()
                                     .map(a -> ArticleDetailResponseConverter.toArticleDetailResponse(a))
                                     .collect(Collectors.toList());
        };
    }

    public DataFetcher allLearnedArticlesDataFetcher() {
        return dataFetchingEnvironment -> {

            String userId = dataFetchingEnvironment.getArgument("userId");
            return findArticleService.findAllLearnedArticle(userId)
                                     .stream()
                                     .map(a -> ArticleDetailResponseConverter.toArticleDetailResponse(a))
                                     .collect(Collectors.toList());
        };
    }

    public DataFetcher famousArticlesDataFetcher() {
        return dataFetchingEnvironment -> findArticleService.findFamousArticle()
                                                            .stream()
                                                            .map(a -> ArticleDetailResponseConverter.toArticleDetailResponse(
                                                                a))
                                                            .collect(Collectors.toList());
    }

    public DataFetcher relatedArticlesDataFetcher() {
        return dataFetchingEnvironment -> {
            String token = (String) dataFetchingEnvironment.getContext();
            String userId = tokenVerifier.getUserId(token);
            return findArticleService.findRelatedArticle(userId);
        };
    }

    ///////////////////////////////
    ///////// Mutations   /////////
    ///////////////////////////////
    public DataFetcher initArticleDataFetcher() {

        return dataFetchingEnvironment -> {
            String token = (String) dataFetchingEnvironment.getContext();
            String userId = tokenVerifier.getUserId(token);

            String articleId = createArticleService.initArticle(userId);

            return CreateResponse.builder().id(articleId).build();
        };
    }

    //    public DataFetcher updateArticleDataFetcher() {
    //        return dataFetchingEnvironment -> {
    //            String token = (String) dataFetchingEnvironment.getContext();
    //            String userId = tokenVerifier.getUserId(token);
    //            String articleId = dataFetchingEnvironment.getArgument("articleId");
    //
    //            Map<String, Object> articleInputMap = (Map) dataFetchingEnvironment.getArgument("article");
    //            updateArticleService.updateArticle(articleId,
    //                                               userId,
    //                                               ArticleInputConverter.toArticleInput(articleInputMap));
    //            return UpdateResponse.builder().build();
    //        };
    //    }

    public DataFetcher updateArticleTitleDataFetcher() {
        return dataFetchingEnvironment -> {
            String token = (String) dataFetchingEnvironment.getContext();
            String userId = tokenVerifier.getUserId(token);
            String articleId = dataFetchingEnvironment.getArgument("articleId");
            String title = dataFetchingEnvironment.getArgument("title");

            updateArticleService.updateArticleTitle(articleId, userId, title);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher updateArticleImageUrlDataFetcher() {
        return dataFetchingEnvironment -> {
            String token = (String) dataFetchingEnvironment.getContext();
            String userId = tokenVerifier.getUserId(token);
            String articleId = dataFetchingEnvironment.getArgument("articleId");
            String imageUrl = dataFetchingEnvironment.getArgument("url");

            updateArticleService.updateArticleImageUrl(articleId, userId, imageUrl);
            return UpdateImageResponse.builder().url(imageUrl).build();
        };
    }

    public DataFetcher updateArticleTagsDataFetcher() {
        return dataFetchingEnvironment -> {
            String token = (String) dataFetchingEnvironment.getContext();
            String userId = tokenVerifier.getUserId(token);
            String articleId = dataFetchingEnvironment.getArgument("articleId");
            List<String> tags = (List<String>) dataFetchingEnvironment.getArgument("tags");

            updateArticleService.updateArticleTags(articleId, userId, tags);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher deleteArticleDataFetcher() {
        return dataFetchingEnvironment -> {
            String token = (String) dataFetchingEnvironment.getContext();
            String userId = tokenVerifier.getUserId(token);
            String articleId = dataFetchingEnvironment.getArgument("articleId");
            deleteArticleService.deleteArticle(articleId, userId);

            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher publishArticleDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            ArticleInput articleInput = ArticleInputConverter.toArticleInput(dataFetchingEnvironment);
            List<UpdateSectionInput> sectionInputs =
                UpdateSectionInputsConverter.toUpdateSectionInputs(dataFetchingEnvironment);
            updateArticleService.publishArticle(articleInput, sectionInputs, requester);

            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher draftArticleDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            ArticleInput articleInput = ArticleInputConverter.toArticleInput(dataFetchingEnvironment);
            List<UpdateSectionInput> sectionInputs =
                UpdateSectionInputsConverter.toUpdateSectionInputs(dataFetchingEnvironment);

            updateArticleService.draftArticle(articleInput, sectionInputs, requester);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher likeArticleDataFetcher() {
        return dataFetchingEnvironment -> {
            String token = (String) dataFetchingEnvironment.getContext();
            String userId = tokenVerifier.getUserId(token);
            String articleId = dataFetchingEnvironment.getArgument("articleId");
            updateArticleService.likeArticle(articleId, userId);

            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher deleteLikeArticleDataFetcher() {
        return dataFetchingEnvironment -> {
            String token = (String) dataFetchingEnvironment.getContext();
            String userId = tokenVerifier.getUserId(token);
            String articleId = dataFetchingEnvironment.getArgument("articleId");
            updateArticleService.deleteLikeArticle(articleId, userId);

            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher finishArticleDataFetcher() {
        return dataFetchingEnvironment -> {
            String token = (String) dataFetchingEnvironment.getContext();
            String userId = tokenVerifier.getUserId(token);
            String articleId = dataFetchingEnvironment.getArgument("articleId");
            updateArticleService.finishArticle(articleId, userId);

            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher deleteFinishArticleDataFetcher() {
        return dataFetchingEnvironment -> {
            String token = (String) dataFetchingEnvironment.getContext();
            String userId = tokenVerifier.getUserId(token);
            String articleId = dataFetchingEnvironment.getArgument("articleId");
            updateArticleService.deleteFinishArticle(articleId, userId);

            return UpdateResponse.builder().build();
        };
    }
}
