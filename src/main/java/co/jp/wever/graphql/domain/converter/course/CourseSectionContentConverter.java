package co.jp.wever.graphql.domain.converter.course;

import co.jp.wever.graphql.application.datamodel.request.course.ContentInput;
import co.jp.wever.graphql.domain.domainmodel.course.content.CourseSectionContent;
import co.jp.wever.graphql.domain.domainmodel.course.content.CourseSectionContentId;
import co.jp.wever.graphql.domain.domainmodel.course.content.CourseSectionContentNumber;

public class CourseSectionContentConverter {
    public static CourseSectionContent toCourseSectionContent(ContentInput input) {
        return CourseSectionContent.of(CourseSectionContentId.of(input.getArticleId()),
                                       CourseSectionContentNumber.of(input.getNumber()));
    }
}
