package co.jp.wever.graphql.domain.factory;

import co.jp.wever.graphql.application.datamodel.request.CourseInput;
import co.jp.wever.graphql.domain.converter.course.CourseSectionsConverter;
import co.jp.wever.graphql.domain.domainmodel.content.ContentBase;
import co.jp.wever.graphql.domain.domainmodel.course.Course;
import co.jp.wever.graphql.domain.domainmodel.course.CourseDescription;
import co.jp.wever.graphql.domain.domainmodel.course.section.CourseSections;
import io.netty.util.internal.StringUtil;

public class CourseFactory {
    public static Course make(CourseInput input) {

        boolean entry = false;
        if (StringUtil.isNullOrEmpty(input.getId())) {
            entry = true;
        }

        ContentBase base = ContentBase.of(input.getTitle(), input.getImageUrl(), input.getTagIds(), entry);
        CourseDescription description = CourseDescription.of(input.getDescription());
        CourseSections sections = CourseSectionsConverter.toCourseSections(input.getSections());

        return Course.of(base, description, sections);
    }
}
