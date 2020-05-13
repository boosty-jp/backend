package jp.boosty.backend.domain.domainmodel.content;

import org.springframework.http.HttpStatus;

import jp.boosty.backend.domain.GraphQLCustomException;
import jp.boosty.backend.infrastructure.constant.GraphQLErrorMessage;
import io.netty.util.internal.StringUtil;

public class ContentDescription {
    private String value;
    private final static int MAX_WORD_COUNT = 500;

    private ContentDescription(String value) {
        this.value = value;
    }

    public static ContentDescription of(String value) {
        if (StringUtil.isNullOrEmpty(value)) {
            return new ContentDescription("");
        }

        if (value.length() > MAX_WORD_COUNT) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.DESCRIPTION_OVER.getString());
        }

        return new ContentDescription(value);
    }

    public String getValue() {
        return value;
    }
}
