package co.jp.wever.graphql.domain.domainmodel.course.content;

import org.springframework.http.HttpStatus;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class CourseSectionContentNumber {
    private int value;
    private final static int MAX_CONTENT_SIZE = 10;

    private CourseSectionContentNumber(int value) {
        this.value = value;
    }

    public static CourseSectionContentNumber of(int value) {
        if (value > MAX_CONTENT_SIZE) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.SECTION_CONTENT_NUMBER_OVER.getString());
        }

        return new CourseSectionContentNumber(value);
    }

    public int getValue() {
        return value;
    }
}
