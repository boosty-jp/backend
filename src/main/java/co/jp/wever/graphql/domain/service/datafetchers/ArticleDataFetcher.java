package co.jp.wever.graphql.domain.service.datafetchers;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import co.jp.wever.graphql.infrastructure.datamodel.Article;
import co.jp.wever.graphql.application.datamodel.CreateResponse;
import co.jp.wever.graphql.application.datamodel.ErrorResponse;
import co.jp.wever.graphql.application.datamodel.UpdateResponse;
import graphql.schema.DataFetcher;

@Component
public class ArticleDataFetcher {

    ///////////////////////////////
    //////////// Query ////////////
    ///////////////////////////////
    public DataFetcher articleDataFetcher() {
        // データ取得
        // 下書きかどうかチェック
        // 下書きだった場合は認証チェックする
        return dataFetchingEnvironment -> Article.builder().id(1L).build();
    }

    public DataFetcher allArticlesDataFetcher() {
        // ユーザー認証
        // データ取得
        return dataFetchingEnvironment -> {
            List<Article> articles = new ArrayList<>();
            return articles;
        };
    }

    public DataFetcher allPublishedArticlesDataFetcher() {
        // データ取得
        return dataFetchingEnvironment -> {
            List<Article> articles = new ArrayList<>();
            return articles;
        };
    }

    public DataFetcher allDraftedArticlesDataFetcher() {
        // ユーザー認証
        // データ取得
        return dataFetchingEnvironment -> {
            List<Article> articles = new ArrayList<>();
            return articles;
        };
    }

    public DataFetcher allLikedArticlesDataFetcher() {
        // データ取得
        return dataFetchingEnvironment -> {
            List<Article> articles = new ArrayList<>();
            return articles;
        };
    }

    public DataFetcher allLearnedArticlesDataFetcher() {
        // 認証
        // データ取得
        return dataFetchingEnvironment -> {
            List<Article> articles = new ArrayList<>();
            return articles;
        };
    }

    public DataFetcher allBookmarkedArticlesDataFetcher() {
        // 認証
        // データ取得
        return dataFetchingEnvironment -> {
            List<Article> articles = new ArrayList<>();
            return articles;
        };
    }

    public DataFetcher famousArticlesDataFetcher() {
        // 認証
        // データ取得
        return dataFetchingEnvironment -> {
            List<Article> articles = new ArrayList<>();
            return articles;
        };
    }

    public DataFetcher relatedArticlesDataFetcher() {
        // 認証
        // データ取得
        return dataFetchingEnvironment -> {
            List<Article> articles = new ArrayList<>();
            return articles;
        };
    }

    ///////////////////////////////
    ///////// Mutations   /////////
    ///////////////////////////////
    public DataFetcher initArticleDataFetcher() {
        // 認証
        // データ追加
        return dataFetchingEnvironment -> CreateResponse.builder().id("1").error(ErrorResponse.builder().errorCode("code").errorMessage("error").build());
    }

    public DataFetcher updateArticlesElementDataFetcher() {
        // 認証
        // 正しいリクエストデータかバリデーションチェック
        // データ更新
        return dataFetchingEnvironment -> UpdateResponse.builder().error(ErrorResponse.builder().errorCode("code").errorMessage("error").build());
    }

    public DataFetcher deleteArticlesElementDataFetcher() {
        // 認証
        // データ削除
        return dataFetchingEnvironment -> UpdateResponse.builder().error(ErrorResponse.builder().errorCode("code").errorMessage("error").build());
    }

    public DataFetcher publishArticlesElementDataFetcher() {
        // 認証
        // データ取得
        // 公開できるかどうかチェック
        // データ削除
        return dataFetchingEnvironment -> UpdateResponse.builder().error(ErrorResponse.builder().errorCode("code").errorMessage("error").build());
    }

    public DataFetcher draftArticlesElementDataFetcher() {
        // 認証
        // データ取得
        // 下書きにできるかどうかチェック
        // データ削除
        return dataFetchingEnvironment -> UpdateResponse.builder().error(ErrorResponse.builder().errorCode("code").errorMessage("error").build());
    }

    public DataFetcher bookmarkArticlesElementDataFetcher() {
        // 認証
        // データ更新
        return dataFetchingEnvironment -> UpdateResponse.builder().error(ErrorResponse.builder().errorCode("code").errorMessage("error").build());
    }

    public DataFetcher likeArticlesElementDataFetcher() {
        // 認証
        // データ更新
        return dataFetchingEnvironment -> UpdateResponse.builder().error(ErrorResponse.builder().errorCode("code").errorMessage("error").build());
    }
}
