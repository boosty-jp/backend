package co.jp.wever.graphql.domain.repository.course;

import org.springframework.stereotype.Repository;

import java.util.List;

import co.jp.wever.graphql.infrastructure.datamodel.course.CourseEntity;

@Repository
public interface CourseQueryRepository {
    CourseEntity findOne(String courseId, String userId);

    CourseEntity findOneForGuest(String courseId);

    String findAuthorId(String courseId);

    List<Long> findLearnList(String courseId, String userId);
}
