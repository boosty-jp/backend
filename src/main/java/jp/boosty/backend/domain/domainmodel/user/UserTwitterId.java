package jp.boosty.backend.domain.domainmodel.user;

import org.springframework.http.HttpStatus;

import jp.boosty.backend.domain.GraphQLCustomException;
import jp.boosty.backend.infrastructure.constant.GraphQLErrorMessage;

public class UserTwitterId {
    private String value;

    // TwitterIDの長さは15文字が最大
    // https://help.twitter.com/ja/managing-your-account/twitter-username-rules
    private final static int MAX_WORD_LENGTH = 15;

    private UserTwitterId(String value) {
        this.value = value;
    }

    public static UserTwitterId of(String value) {
        if (value.length() > MAX_WORD_LENGTH) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_SNS_ID.getString());
        }

        return new UserTwitterId(value);
    }

    public String getValue() {
        return value;
    }
}
