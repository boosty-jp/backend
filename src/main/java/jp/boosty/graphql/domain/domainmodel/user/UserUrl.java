package jp.boosty.graphql.domain.domainmodel.user;

import org.springframework.http.HttpStatus;

import jp.boosty.graphql.domain.GraphQLCustomException;
import jp.boosty.graphql.infrastructure.constant.GraphQLErrorMessage;

public class UserUrl {
    private String value;

    private final static int MAX_URL_SIZE = 2048;

    private UserUrl(String value) {
        this.value = value;
    }

    public static UserUrl of(String value) {
        if (value.length() > MAX_URL_SIZE) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_USER_URL.getString());
        }

        // TODO: URL先のリンクが存在するかどうかチェックしたい
        // TODO: URL先のリンクが安全かどうかチェックしたい

        return new UserUrl(value);
    }

    public String getValue() {
        return value;
    }
}
