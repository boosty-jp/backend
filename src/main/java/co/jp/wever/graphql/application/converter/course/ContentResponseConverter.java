package co.jp.wever.graphql.application.converter.course;

import java.util.stream.Collectors;

import co.jp.wever.graphql.application.converter.skill.SkillResponseConverter;
import co.jp.wever.graphql.application.datamodel.response.query.course.ContentResponse;
import co.jp.wever.graphql.infrastructure.datamodel.course.CourseSectionContentEntity;

public class ContentResponseConverter {
    public static ContentResponse toContentResponse(CourseSectionContentEntity content) {
        return ContentResponse.builder()
                              .id(content.getId())
                              .title(content.getTitle())
                              .number(content.getNumber())
                              .skills(content.getSkills()
                                             .stream()
                                             .map(s -> SkillResponseConverter.toSkillResponse(s))
                                             .collect(Collectors.toList()))
                              .learned(content.isLearned())
                              .build();

    }
}
