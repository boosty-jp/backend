package jp.boosty.backend.domain.domainmodel.tag;

import org.springframework.http.HttpStatus;

import jp.boosty.backend.domain.GraphQLCustomException;
import jp.boosty.backend.infrastructure.constant.GraphQLErrorMessage;
import jp.boosty.backend.infrastructure.util.AbuseWordDetector;
import io.netty.util.internal.StringUtil;

public class TagName {

    private String value;

    private TagName(String value) {
        this.value = value;
    }

    public static TagName of(String value) {
        if (StringUtil.isNullOrEmpty(value)) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.TAG_NAME_EMPTY.getString());
        }

        if (AbuseWordDetector.isAbuseWord(value)) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_TAG_NAME.getString());
        }

        return new TagName(value);
    }

    public String getValue() {
        return value;
    }
}
