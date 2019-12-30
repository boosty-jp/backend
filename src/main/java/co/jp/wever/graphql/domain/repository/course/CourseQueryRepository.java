package co.jp.wever.graphql.domain.repository.course;

import org.springframework.stereotype.Repository;

import java.util.List;

import co.jp.wever.graphql.domain.domainmodel.search.SearchCondition;
import co.jp.wever.graphql.infrastructure.datamodel.course.CourseEntity;
import co.jp.wever.graphql.infrastructure.datamodel.course.CourseListEntity;

@Repository
public interface CourseQueryRepository {
    CourseEntity findOne(String courseId, String userId);

    CourseEntity findOneForGuest(String courseId);

    String findAuthorId(String courseId);

    List<Long> findLearnList(String courseId, String userId);

    List<CourseEntity> findCreated(String userId, SearchCondition searchCondition);

    CourseListEntity findCreatedBySelf(String userId, SearchCondition searchCondition);

    List<CourseEntity> findActioned(String userId, SearchCondition searchCondition);

    List<CourseEntity> findFamous();
}
