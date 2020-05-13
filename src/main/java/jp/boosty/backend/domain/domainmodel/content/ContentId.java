package jp.boosty.backend.domain.domainmodel.content;

import org.springframework.http.HttpStatus;

import jp.boosty.backend.domain.GraphQLCustomException;
import jp.boosty.backend.infrastructure.constant.GraphQLErrorMessage;
import io.netty.util.internal.StringUtil;

public class ContentId {
    private String value;
    private final static int MAX_WORD_COUNT = 60;

    private ContentId(String value) {
        this.value = value;
    }

    public static ContentId of(String value) {
        if (StringUtil.isNullOrEmpty(value)) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_BOOK_DATA.getString());
        }

        if (value.length() > MAX_WORD_COUNT) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.TITLE_OVER.getString());
        }

        return new ContentId(value);
    }

    public String getValue() {
        return value;
    }
}
