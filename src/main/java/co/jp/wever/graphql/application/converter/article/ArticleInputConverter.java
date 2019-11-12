package co.jp.wever.graphql.application.converter.article;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

import co.jp.wever.graphql.application.datamodel.request.ArticleInput;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import graphql.schema.DataFetchingEnvironment;

public class ArticleInputConverter {
    public static ArticleInput toArticleInput(DataFetchingEnvironment request) {
        try {
            Map<String, Object> article = request.getArgument("article");

            return ArticleInput.builder()
                               .id(article.get("id").toString())
                               .title(article.get("title").toString())
                               .imageUrl(article.get("imageUrl").toString())
                               .tags((List<String>) article.get("tags"))
                               .build();
        } catch (Exception e) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }
}
