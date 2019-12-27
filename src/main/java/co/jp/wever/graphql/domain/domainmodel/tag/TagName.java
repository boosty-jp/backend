package co.jp.wever.graphql.domain.domainmodel.tag;

import org.springframework.http.HttpStatus;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import co.jp.wever.graphql.infrastructure.util.AbuseWordDetector;
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
