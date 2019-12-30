package co.jp.wever.graphql.domain.domainmodel.course.section;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.domainmodel.course.content.CourseSectionContent;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class CourseSections {
    private List<CourseSection> sections;
    private final static int MAX_SIZE = 10;

    private CourseSections(List<CourseSection> sections) {
        this.sections = sections;
    }

    public static CourseSections of(List<CourseSection> sections) {
        if (Objects.isNull(sections)) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.SECTION_NULL.getString());
        }

        if (sections.size() > MAX_SIZE) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.SECTION_SIZE_OVER.getString());
        }

        return new CourseSections(sections);
    }

    public List<CourseSection> getSections() {
        return sections;
    }

    private boolean duplicatedSectionNumber() {
        return sections.stream().map(s -> s.getNumber().getValue()).collect(Collectors.toSet()).size()
            != sections.size();
    }

    private boolean duplicatedContentId() {
        List<CourseSectionContent> contents =
            sections.stream().flatMap(s -> s.getContents().getContents().stream()).collect(Collectors.toList());

        return contents.stream().map(c -> c.getId().getValue()).collect(Collectors.toSet()).size() != contents.size();
    }

    public boolean valid() {
        if (duplicatedSectionNumber()) {
            return false;
        }

        return !duplicatedContentId();
    }
}
