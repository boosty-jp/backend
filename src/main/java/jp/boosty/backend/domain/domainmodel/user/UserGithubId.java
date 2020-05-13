package jp.boosty.backend.domain.domainmodel.user;

import org.springframework.http.HttpStatus;

import jp.boosty.backend.domain.GraphQLCustomException;
import jp.boosty.backend.infrastructure.constant.GraphQLErrorMessage;

public class UserGithubId {

    private String value;

    // IDの長さは公開されていないが、制限する
    private final static int MAX_WORD_LENGTH = 128;

    private UserGithubId(String value) {
        this.value = value;
    }

    public static UserGithubId of(String value) {
        if (value.length() > MAX_WORD_LENGTH) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_SNS_ID.getString());
        }

        return new UserGithubId(value);
    }

    public String getValue() {
        return value;
    }
}
