package co.jp.wever.graphql.domain.domainmodel.section;

import org.springframework.http.HttpStatus;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class SectionNumber {
    private final static long MIN_NUMBER = 1;
    private final static long MAX_NUMBER = 30;
    private long value;

    private SectionNumber(long value) {
        this.value = value;
    }

    public static SectionNumber of(long value) {
        if (value < MIN_NUMBER) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.SECTION_NUMBER_INVALID.getString());
        }

        if (value > MAX_NUMBER) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.SECTION_NUMBER_INVALID.getString());
        }

        return new SectionNumber(value);
    }

    public long getValue() {
        return value;
    }

    public boolean isSame(SectionNumber target) {
        return target.value == value;
    }
}
