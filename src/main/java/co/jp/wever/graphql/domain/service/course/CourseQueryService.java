package co.jp.wever.graphql.domain.service.course;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import co.jp.wever.graphql.application.datamodel.request.Requester;
import co.jp.wever.graphql.application.datamodel.request.SearchConditionInput;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.domainmodel.search.SearchCondition;
import co.jp.wever.graphql.domain.factory.SearchConditionFactory;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import co.jp.wever.graphql.infrastructure.constant.edge.EdgeLabel;
import co.jp.wever.graphql.infrastructure.datamodel.article.ArticleEntity;
import co.jp.wever.graphql.infrastructure.datamodel.course.CourseEntity;
import co.jp.wever.graphql.infrastructure.datamodel.user.UserEntity;
import co.jp.wever.graphql.infrastructure.repository.course.CourseQueryRepositoryImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CourseQueryService {
    private final CourseQueryRepositoryImpl courseQueryRepository;

    public CourseQueryService(CourseQueryRepositoryImpl courseQueryRepository) {
        this.courseQueryRepository = courseQueryRepository;
    }

    public CourseEntity findCourse(String courseId, Requester requester) {
        CourseEntity courseEntity;
        if (requester.isGuest()) {
            courseEntity = courseQueryRepository.findOneForGuest(courseId);
        } else {
            courseEntity = courseQueryRepository.findOne(courseId, requester.getUserId());
        }

        if (!canRead(courseEntity, requester.getUserId())) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        return courseEntity;
    }

    public List<CourseEntity> findCreatedCourse(String userId, SearchConditionInput searchConditionInput) {
        SearchCondition searchCondition = SearchConditionFactory.makeFilteredPublished(searchConditionInput);

        return courseQueryRepository.findCreated(userId, searchCondition);
    }

    public List<CourseEntity> findCreatedCoursesBySelf(
        Requester requester, SearchConditionInput searchConditionInput) {
        SearchCondition searchCondition = SearchConditionFactory.make(searchConditionInput);

        if (!searchCondition.createdFilter()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_SEARCH_CONDITION.getString());
        }

        return courseQueryRepository.findCreatedBySelf(requester.getUserId(), searchCondition);
    }

    public List<CourseEntity> findActionedCourses(String userId, SearchConditionInput searchConditionInput) {
        SearchCondition searchCondition = SearchConditionFactory.make(searchConditionInput);

        if (!searchCondition.validActionedFilter()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_SEARCH_CONDITION.getString());
        }

        UserEntity userEntity = this.findUserRepository.findOne(userId);

        if (!searchCondition.canSearch(userEntity.getLikePublic(), userEntity.getLearnPublic())) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.SEARCH_FORBIDDEN.getString());
        }

        return courseQueryRepository.findActioned(userId, searchCondition);
    }

    public List<CourseEntity> findActionedCoursesBySelf(
        Requester requester, SearchConditionInput searchConditionInput) {
        SearchCondition searchCondition = SearchConditionFactory.make(searchConditionInput);

        if (!searchCondition.validActionedFilter()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_SEARCH_CONDITION.getString());
        }

        return courseQueryRepository.findActioned(requester.getUserId(), searchCondition);
    }

    //TODO: ドメイン化
    private boolean canRead(CourseEntity courseEntity, String requesterId) {
        if (courseEntity.getBase().getStatus().equals(EdgeLabel.PUBLISH.getString())) {
            return true;
        }

        return requesterId.equals(courseEntity.getAuthor().getUserId());
    }
}
