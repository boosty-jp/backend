package co.jp.wever.graphql.domain.converter.course;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.application.datamodel.request.SectionInput;
import co.jp.wever.graphql.domain.domainmodel.course.section.CourseSections;

public class CourseSectionsConverter {
    public static CourseSections toCourseSections(List<SectionInput> input) {
        return CourseSections.of(input.stream()
                                      .map(s -> CourseSectionConverter.toCourseSection(s))
                                      .collect(Collectors.toList()));
    }
}
