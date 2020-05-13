package jp.boosty.graphql.domain.domainmodel.content;

import org.springframework.http.HttpStatus;

import jp.boosty.graphql.domain.GraphQLCustomException;
import jp.boosty.graphql.infrastructure.constant.GraphQLErrorMessage;
import io.netty.util.internal.StringUtil;

public class ContentTitle {
    private String value;
    private final static int MAX_WORD_COUNT = 60;

    private ContentTitle(String value) {
        this.value = value;
    }

    public static ContentTitle of(String value) {
        if (StringUtil.isNullOrEmpty(value)) {
            return new ContentTitle("");
        }

        if (value.length() > MAX_WORD_COUNT) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.TITLE_OVER.getString());
        }

        return new ContentTitle(value);
    }

    public boolean hasValue() {
        return !StringUtil.isNullOrEmpty(value);
    }

    public String getValue() {
        return value;
    }
}
