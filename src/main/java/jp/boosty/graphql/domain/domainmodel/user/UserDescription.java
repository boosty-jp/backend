package jp.boosty.graphql.domain.domainmodel.user;

import org.springframework.http.HttpStatus;

import jp.boosty.graphql.domain.GraphQLCustomException;
import jp.boosty.graphql.infrastructure.constant.GraphQLErrorMessage;

public class UserDescription {

    private String value;
    private final static int MAX_WORD_COUNT = 200;

    private UserDescription(String value) {
        this.value = value;
    }

    public static UserDescription of(String value) {
        if (value.length() > MAX_WORD_COUNT) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.USER_DESCRIPTION_OVER.getString());
        }

        return new UserDescription(value);
    }

    public String getValue() {
        return value;
    }
}
