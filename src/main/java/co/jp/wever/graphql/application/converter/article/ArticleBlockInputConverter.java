package co.jp.wever.graphql.application.converter.article;

import org.springframework.http.HttpStatus;

import java.util.Map;

import co.jp.wever.graphql.application.datamodel.request.ArticleBlockInput;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class ArticleBlockInputConverter {
    public static ArticleBlockInput toArticleBlockInput(Map<String, Object> input) {
        try {
            return ArticleBlockInput.builder()
                                    .type(input.get("type").toString())
                                    .data(input.get("data").toString())
                                    .build();
        } catch (Exception e) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }
}
