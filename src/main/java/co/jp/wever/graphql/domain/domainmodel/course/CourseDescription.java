package co.jp.wever.graphql.domain.domainmodel.course;

import org.springframework.http.HttpStatus;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import io.netty.util.internal.StringUtil;

public class CourseDescription {
    private String value;
    private final static int MAX_WORD_COUNT = 300;

    private CourseDescription(String value) {
        this.value = value;
    }

    public static CourseDescription of(String value) {
        if (StringUtil.isNullOrEmpty(value)) {
            return new CourseDescription("");
        }

        if (value.length() > MAX_WORD_COUNT) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.DESCRIPTION_OVER.getString());
        }

        return new CourseDescription(value);
    }

    public String getValue() {
        return value;
    }
}
