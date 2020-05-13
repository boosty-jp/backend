package jp.boosty.backend.domain.domainmodel.user;

import org.springframework.http.HttpStatus;

import jp.boosty.backend.domain.GraphQLCustomException;
import jp.boosty.backend.infrastructure.constant.GraphQLErrorMessage;
import io.netty.util.internal.StringUtil;

public class UserDisplayName {

    private String value;
    private final static int MAX_WORD_COUNT = 30;

    private UserDisplayName(String value) {
        this.value = value;
    }

    public static UserDisplayName of(String value) {
        if (value.length() > MAX_WORD_COUNT) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.USER_NAME_OVER.getString());
        }

        if (StringUtil.isNullOrEmpty(value)) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.USER_NAME_EMPTY.getString());
        }

        return new UserDisplayName(value);
    }

    public String getValue() {
        return value;
    }
}
