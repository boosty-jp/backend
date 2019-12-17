package co.jp.wever.graphql.domain.domainmodel.user;

import org.springframework.http.HttpStatus;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class UserFacebookId {

    private String value;

    // IDの長さは公開されていないが、制限する
    private final static int MAX_WORD_LENGTH = 128;

    private UserFacebookId(String value) {
        this.value = value;
    }

    public static UserFacebookId of(String value) {
        if (value.length() > MAX_WORD_LENGTH) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_SNS_ID.getString());
        }

        return new UserFacebookId(value);
    }

    public String getValue() {
        return value;
    }
}
