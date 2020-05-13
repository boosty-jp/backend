package jp.boosty.graphql.domain.domainmodel.book;

import org.springframework.http.HttpStatus;

import jp.boosty.graphql.domain.GraphQLCustomException;
import jp.boosty.graphql.infrastructure.constant.GraphQLErrorMessage;
import io.netty.util.internal.StringUtil;

public class BookDescription {
    private String value;
    private final static int MIN_WORD_COUNT = 50;
    private final static int MAX_WORD_COUNT = 500;

    private BookDescription(String value) {
        this.value = value;
    }

    public static BookDescription of(String value) {
        if (StringUtil.isNullOrEmpty(value) || value.length() < MIN_WORD_COUNT) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.DESCRIPTION_OVER.getString());
        }

        if (value.length() > MAX_WORD_COUNT) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.DESCRIPTION_OVER.getString());
        }

        return new BookDescription(value);
    }

    public String getValue() {
        return value;
    }
}
