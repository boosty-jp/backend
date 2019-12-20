package co.jp.wever.graphql.application.converter.course;

import java.util.Date;
import java.util.stream.Collectors;

import co.jp.wever.graphql.application.datamodel.response.query.course.SectionResponse;
import co.jp.wever.graphql.infrastructure.datamodel.course.CourseSectionEntity;
import co.jp.wever.graphql.infrastructure.util.DateToStringConverter;

public class SectionResponseConverter {
    public static SectionResponse toSectionResponse(CourseSectionEntity section) {
        return SectionResponse.builder()
                              .id(section.getId())
                              .title(section.getTitle())
                              .number(section.getNumber())
                              .contents(section.getCourseSectionContents()
                                               .stream()
                                               .map(c -> ContentResponseConverter.toContentResponse(c))
                                               .collect(Collectors.toList()))
                              .createDate(DateToStringConverter.toDateString(new Date(section.getCreatedDate())))
                              .build();
    }
}
