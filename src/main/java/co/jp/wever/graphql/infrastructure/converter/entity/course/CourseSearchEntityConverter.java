package co.jp.wever.graphql.infrastructure.converter.entity.course;

import co.jp.wever.graphql.domain.domainmodel.course.Course;
import co.jp.wever.graphql.infrastructure.datamodel.algolia.CourseSearchEntity;

public class CourseSearchEntityConverter {
    public static CourseSearchEntity toCourseSearchEntity(
        String courseId,
        Course course,
        String authorId,
        long updateDate) {
        return CourseSearchEntity.builder()
                                 .objectID(courseId)
                                 .title(course.getBase().getTitle().getValue())
                                 .description(course.getDescription().getValue())
                                 .imageUrl(course.getBase().getImageUrl().getValue())
                                 .authorId(authorId)
                                 .articleCount(course.getArticleIds().size())
                                 .sectionCount(course.getSections().getSections().size())
                                 .likeCount(0)
                                 .learnedCount(0)
                                 .viewCount(0)
                                 .updateDate(updateDate)
                                 .build();
    }
}
