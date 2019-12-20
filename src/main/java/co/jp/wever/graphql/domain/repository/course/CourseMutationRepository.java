package co.jp.wever.graphql.domain.repository.course;

import org.springframework.stereotype.Repository;

import co.jp.wever.graphql.domain.domainmodel.course.Course;

@Repository
public interface CourseMutationRepository {
    void publish(String courseId, Course course, String userId);

    String publishByEntry(Course course, String userId);

    void draft(String courseId, Course course, String userId);

    String draftByEntry(Course course, String userId);

    void delete(String courseId, String userId);

    void like(String courseId, String userId);

    void clearLike(String courseId, String userId);

    void start(String courseId, String userId);

    void clearStart(String courseId, String userId);

    void complete(String courseId, String userId);

}
