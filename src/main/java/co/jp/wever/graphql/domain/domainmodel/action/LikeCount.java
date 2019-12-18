package co.jp.wever.graphql.domain.domainmodel.action;

import org.springframework.http.HttpStatus;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class LikeCount {

    private long value;
    private final static long MIN_VALUE = 0;
    private final static long MAX_VALUE = 999_999_999;

    private LikeCount(long value) {
        this.value = value;
    }

    public static LikeCount of(long value) {
        if (value < MIN_VALUE) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }

        if (value > MAX_VALUE) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }

        return new LikeCount(value);
    }

    public long getValue() {
        return value;
    }
}
