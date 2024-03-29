package jp.boosty.backend.domain.domainmodel.page;

import org.springframework.http.HttpStatus;

import jp.boosty.backend.domain.GraphQLCustomException;
import jp.boosty.backend.infrastructure.constant.GraphQLErrorMessage;
import io.netty.util.internal.StringUtil;

public class PageText {
    private String value;
    private final static int MAX_WORD_COUNT = 30000;

    private PageText(String value) {
        this.value = value;
    }

    public static PageText of(String value) {
        if (StringUtil.isNullOrEmpty(value)) {
            return new PageText("");
        }

        if (value.length() > MAX_WORD_COUNT) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.TOO_LONG_PAGE_CONTENT.getString());
        }

        return new PageText(value);
    }

    public String getValue() {
        return value;
    }
}
