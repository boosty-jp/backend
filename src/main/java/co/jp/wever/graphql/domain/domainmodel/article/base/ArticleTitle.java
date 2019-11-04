package co.jp.wever.graphql.domain.domainmodel.article.base;

import org.springframework.http.HttpStatus;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class ArticleTitle {

    private String value;
    private final static int MAX_WORD_COUNT = 100;

    private ArticleTitle(String value) {
        this.value = value;
    }

    public static ArticleTitle of(String value) {
        if (value.length() > MAX_WORD_COUNT) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.TITLE_OVER.getString());
        }

        return new ArticleTitle(value);
    }

    public String getValue() {
        return value;
    }
}
