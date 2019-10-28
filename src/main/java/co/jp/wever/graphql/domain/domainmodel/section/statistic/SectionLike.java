package co.jp.wever.graphql.domain.domainmodel.section.statistic;


import org.springframework.http.HttpStatus;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class SectionLike {
    private final static int MIN_NUMBER = 0;
    private int value;

    private SectionLike(int value) {
        this.value = value;
    }

    public static SectionLike of(int value) {
        if (value < MIN_NUMBER) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }

        return new SectionLike(value);
    }

    public int getValue() {
        return value;
    }
}
