package co.jp.wever.graphql.domain.domainmodel.user;

import org.springframework.http.HttpStatus;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class UserId {
    private String value;

    private UserId(String value) {
        this.value = value;
    }

    public static UserId of(String value) {
        if (value.isEmpty()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.USER_ID_EMPTY.getString());
        }

        return new UserId(value);
    }

    public String getValue() {
        return value;
    }

    public boolean same(UserId targetId) {
        return value.equals(targetId.getValue());
    }
}
