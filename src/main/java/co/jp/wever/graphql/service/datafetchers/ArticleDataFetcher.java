package co.jp.wever.graphql.service.datafetchers;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import co.jp.wever.graphql.datamodel.Article;
import co.jp.wever.graphql.datamodel.CreateResponse;
import co.jp.wever.graphql.datamodel.ErrorResponse;
import co.jp.wever.graphql.datamodel.UpdateResponse;
import graphql.schema.DataFetcher;

@Component
public class ArticleDataFetcher {

    ///////////////////////////////
    //////////// Query ////////////
    ///////////////////////////////
    public DataFetcher articleDataFetcher() {
        return dataFetchingEnvironment -> Article.builder().id(1L).build();
    }

    public DataFetcher allArticlesDataFetcher() {
        return dataFetchingEnvironment -> {
            List<Article> articles = new ArrayList<>();
            return articles;
        };
    }

    public DataFetcher allPublishedArticlesDataFetcher() {
        return dataFetchingEnvironment -> {
            List<Article> articles = new ArrayList<>();
            return articles;
        };
    }

    public DataFetcher allDraftedArticlesDataFetcher() {
        return dataFetchingEnvironment -> {
            List<Article> articles = new ArrayList<>();
            return articles;
        };
    }

    public DataFetcher allLikedArticlesDataFetcher() {
        return dataFetchingEnvironment -> {
            List<Article> articles = new ArrayList<>();
            return articles;
        };
    }

    public DataFetcher allLearnedArticlesDataFetcher() {
        return dataFetchingEnvironment -> {
            List<Article> articles = new ArrayList<>();
            return articles;
        };
    }

    public DataFetcher allBookmarkedArticlesDataFetcher() {
        return dataFetchingEnvironment -> {
            List<Article> articles = new ArrayList<>();
            return articles;
        };
    }

    public DataFetcher famousArticlesDataFetcher() {
        return dataFetchingEnvironment -> {
            List<Article> articles = new ArrayList<>();
            return articles;
        };
    }

    public DataFetcher relatedArticlesDataFetcher() {
        return dataFetchingEnvironment -> {
            List<Article> articles = new ArrayList<>();
            return articles;
        };
    }

    ///////////////////////////////
    ///////// Mutations   /////////
    ///////////////////////////////
    public DataFetcher initArticleDataFetcher() {
        return dataFetchingEnvironment -> CreateResponse.builder().id("1").error(ErrorResponse.builder().errorCode("code").errorMessage("error").build());
    }

    public DataFetcher updateArticlesElementDataFetcher() {
        return dataFetchingEnvironment -> UpdateResponse.builder().error(ErrorResponse.builder().errorCode("code").errorMessage("error").build());
    }

    public DataFetcher deleteArticlesElementDataFetcher() {
        return dataFetchingEnvironment -> UpdateResponse.builder().error(ErrorResponse.builder().errorCode("code").errorMessage("error").build());
    }

    public DataFetcher publishArticlesElementDataFetcher() {
        return dataFetchingEnvironment -> UpdateResponse.builder().error(ErrorResponse.builder().errorCode("code").errorMessage("error").build());
    }

    public DataFetcher draftArticlesElementDataFetcher() {
        return dataFetchingEnvironment -> UpdateResponse.builder().error(ErrorResponse.builder().errorCode("code").errorMessage("error").build());
    }

    public DataFetcher bookmarkArticlesElementDataFetcher() {
        return dataFetchingEnvironment -> UpdateResponse.builder().error(ErrorResponse.builder().errorCode("code").errorMessage("error").build());
    }

    public DataFetcher likeArticlesElementDataFetcher() {
        return dataFetchingEnvironment -> UpdateResponse.builder().error(ErrorResponse.builder().errorCode("code").errorMessage("error").build());
    }
}
