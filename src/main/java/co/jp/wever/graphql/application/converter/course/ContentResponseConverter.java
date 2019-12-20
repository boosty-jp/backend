package co.jp.wever.graphql.application.converter.course;

import co.jp.wever.graphql.application.datamodel.response.query.course.ContentResponse;
import co.jp.wever.graphql.infrastructure.datamodel.course.CourseSectionContentEntity;

public class ContentResponseConverter {
    public static ContentResponse toContentResponse(CourseSectionContentEntity content) {
        return ContentResponse.builder()
                              .id(content.getId())
                              .title(content.getTitle())
                              .number(content.getNumber())
                              .learned(content.isLearned())
                              .build();

    }
}
