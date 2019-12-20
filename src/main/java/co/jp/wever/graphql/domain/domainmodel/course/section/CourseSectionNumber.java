package co.jp.wever.graphql.domain.domainmodel.course.section;

import org.springframework.http.HttpStatus;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class CourseSectionNumber {
    private int value;
    private final static int MAX_VALUE = 10;

    private CourseSectionNumber(int value) {
        this.value = value;
    }

    public static CourseSectionNumber of(int value) {
        if (value > MAX_VALUE) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.SECTION_CONTENT_NUMBER_OVER.getString());
        }

        return new CourseSectionNumber(value);
    }

    public int getValue() {
        return value;
    }
}
