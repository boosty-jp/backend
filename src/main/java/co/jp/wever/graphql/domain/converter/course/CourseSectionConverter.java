package co.jp.wever.graphql.domain.converter.course;

import java.util.stream.Collectors;

import co.jp.wever.graphql.application.datamodel.request.course.SectionInput;
import co.jp.wever.graphql.domain.domainmodel.content.ContentTitle;
import co.jp.wever.graphql.domain.domainmodel.course.content.CourseSectionContents;
import co.jp.wever.graphql.domain.domainmodel.course.section.CourseSection;
import co.jp.wever.graphql.domain.domainmodel.course.section.CourseSectionNumber;

public class CourseSectionConverter {
    public static CourseSection toCourseSection(SectionInput input) {
        return CourseSection.of(ContentTitle.of(input.getTitle()),
                                CourseSectionNumber.of(input.getNumber()),
                                CourseSectionContents.of(input.getContents()
                                                              .stream()
                                                              .map(s -> CourseSectionContentConverter.toCourseSectionContent(
                                                                  s))
                                                              .collect(Collectors.toList())));
    }
}
