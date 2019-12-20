package co.jp.wever.graphql.domain.domainmodel.course.section;

import org.springframework.http.HttpStatus;

import java.util.Objects;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.domainmodel.content.ContentTitle;
import co.jp.wever.graphql.domain.domainmodel.course.content.CourseSectionContents;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class CourseSection {
    private ContentTitle title;
    private CourseSectionNumber number;
    private CourseSectionContents contents;

    private CourseSection(
        ContentTitle title, CourseSectionNumber number, CourseSectionContents contents) {
        this.title = title;
        this.number = number;
        this.contents = contents;
    }

    public static CourseSection of(ContentTitle title, CourseSectionNumber number, CourseSectionContents content) {
        if (Objects.isNull(title) || Objects.isNull(number) || Objects.isNull(content)) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.SECTION_NULL.getString());
        }

        return new CourseSection(title, number, content);
    }

    public ContentTitle getTitle() {
        return title;
    }

    public CourseSectionNumber getNumber() {
        return number;
    }

    public CourseSectionContents getContents() {
        return contents;
    }
}
