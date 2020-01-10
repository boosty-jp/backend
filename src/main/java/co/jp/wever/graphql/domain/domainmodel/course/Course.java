package co.jp.wever.graphql.domain.domainmodel.course;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.domainmodel.content.ContentBase;
import co.jp.wever.graphql.domain.domainmodel.content.ContentDescription;
import co.jp.wever.graphql.domain.domainmodel.course.section.CourseSections;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class Course {
    private ContentBase base;
    private ContentDescription description; //TODO: baseに合わせた方がよさそう
    private CourseSections sections;

    private Course(
        ContentBase base, ContentDescription description, CourseSections sections) {
        this.base = base;
        this.description = description;
        this.sections = sections;
    }

    public static Course of(ContentBase base, ContentDescription description, CourseSections sections) {
        if (Objects.isNull(base) || Objects.isNull(description) || Objects.isNull(sections)) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_COURSE_DATA.getString());
        }

        return new Course(base, description, sections);
    }

    public ContentBase getBase() {
        return base;
    }

    public ContentDescription getDescription() {
        return description;
    }

    public CourseSections getSections() {
        return sections;
    }

    public boolean valid() {
        return sections.valid();
    }

    public List<String> getArticleIds() {
        return sections.getSections()
                       .stream()
                       .flatMap(s -> s.getContents().getContents().stream())
                       .collect(Collectors.toList())
                       .stream()
                       .map(c -> c.getId().getValue())
                       .collect(Collectors.toList());
    }
}
