package co.jp.wever.graphql.domain.domainmodel.course.content;

import org.springframework.http.HttpStatus;

import java.util.Objects;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class CourseSectionContent {
    private CourseSectionContentId id;
    private CourseSectionContentNumber number;


    private CourseSectionContent(
        CourseSectionContentId id, CourseSectionContentNumber number) {
        this.id = id;
        this.number = number;
    }

    public static CourseSectionContent of(CourseSectionContentId id, CourseSectionContentNumber number) {
        if (Objects.isNull(id) || Objects.isNull(number)) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_SECTION_CONTENT.getString());
        }

        return new CourseSectionContent(id, number);
    }

    public CourseSectionContentId getId() {
        return id;
    }

    public CourseSectionContentNumber getNumber() {
        return number;
    }
}
