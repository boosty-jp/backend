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
import co.jp.wever.graphql.application.datamodel.response.mutation.CreateResponse;
import co.jp.wever.graphql.application.datamodel.response.mutation.UpdateImageResponse;
import co.jp.wever.graphql.application.datamodel.response.mutation.UpdateResponse;
import co.jp.wever.graphql.domain.service.article.CreateArticleService;
import co.jp.wever.graphql.domain.service.article.DeleteArticleService;
import co.jp.wever.graphql.domain.service.article.FindArticleService;
import co.jp.wever.graphql.domain.service.article.UpdateArticleService;
import graphql.schema.DataFetcher;

@Component
public class ArticleDataFetcher {

    private final FindArticleService findArticleService;
    private final CreateArticleService createArticleService;
    private final UpdateArticleService updateArticleService;
    private final DeleteArticleService deleteArticleService;
    private final RequesterConverter requesterConverter;

    public ArticleDataFetcher(
        FindArticleService findArticleService,
        CreateArticleService createArticleService,
        UpdateArticleService updateArticleService,
        DeleteArticleService deleteArticleService,
        RequesterConverter requesterConverter) {
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
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String articleId = dataFetchingEnvironment.getArgument("articleId");

            return ArticleDetailResponseConverter.toArticleDetailResponse(findArticleService.findArticleDetail(articleId,
                                                                                                               requester));
        };
    }

    public DataFetcher allArticlesDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            return findArticleService.findAllArticle(requester)
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
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            return findArticleService.findAllDraftedArticle(requester)
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

    ///////////////////////////////
    ///////// Mutations   /////////
    ///////////////////////////////
    public DataFetcher initArticleDataFetcher() {

        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String articleId = createArticleService.initArticle(requester);

            return CreateResponse.builder().id(articleId).build();
        };
    }

    public DataFetcher updateArticleTitleDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String articleId = dataFetchingEnvironment.getArgument("articleId");
            String title = dataFetchingEnvironment.getArgument("title");

            updateArticleService.updateArticleTitle(articleId, requester, title);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher updateArticleImageUrlDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String articleId = dataFetchingEnvironment.getArgument("articleId");
            String imageUrl = dataFetchingEnvironment.getArgument("url");

            updateArticleService.updateArticleImageUrl(articleId, requester, imageUrl);
            return UpdateImageResponse.builder().url(imageUrl).build();
        };
    }

    public DataFetcher updateArticleTagsDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String articleId = dataFetchingEnvironment.getArgument("articleId");
            List<String> tags = (List<String>) dataFetchingEnvironment.getArgument("tags");

            updateArticleService.updateArticleTags(articleId, requester, tags);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher deleteArticleDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String articleId = dataFetchingEnvironment.getArgument("articleId");
            deleteArticleService.deleteArticle(articleId, requester);

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
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String articleId = dataFetchingEnvironment.getArgument("articleId");
            updateArticleService.likeArticle(articleId, requester);

            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher deleteLikeArticleDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String articleId = dataFetchingEnvironment.getArgument("articleId");
            updateArticleService.deleteLikeArticle(articleId, requester);

            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher finishArticleDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String articleId = dataFetchingEnvironment.getArgument("articleId");
            updateArticleService.finishArticle(articleId, requester);

            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher deleteFinishArticleDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String articleId = dataFetchingEnvironment.getArgument("articleId");
            updateArticleService.deleteFinishArticle(articleId, requester);

            return UpdateResponse.builder().build();
        };
    }
}
