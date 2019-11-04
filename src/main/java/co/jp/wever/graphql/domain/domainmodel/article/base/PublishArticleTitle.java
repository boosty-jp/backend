package co.jp.wever.graphql.domain.domainmodel.article.base;

import org.springframework.http.HttpStatus;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class PublishArticleTitle {

    private String value;
    private final static int MIN_WORD_COUNT = 1;
    private final static int MAX_WORD_COUNT = 100;

    private PublishArticleTitle(String value) {
        this.value = value;
    }

    public static PublishArticleTitle of(String value) {
        if (value.length() < MIN_WORD_COUNT) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                            GraphQLErrorMessage.EMPTY_TITLE.getString());
        }

        if (value.length() > MAX_WORD_COUNT) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.TITLE_OVER.getString());
        }

        return new PublishArticleTitle(value);
    }

    public String getValue() {
        return value;
    }
}
