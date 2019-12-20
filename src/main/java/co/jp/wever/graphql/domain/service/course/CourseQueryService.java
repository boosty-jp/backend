package co.jp.wever.graphql.domain.service.course;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import co.jp.wever.graphql.application.datamodel.request.Requester;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import co.jp.wever.graphql.infrastructure.constant.edge.EdgeLabel;
import co.jp.wever.graphql.infrastructure.datamodel.course.CourseEntity;
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

    //TODO: ドメイン化
    private boolean canRead(CourseEntity courseEntity, String requesterId) {
        if (courseEntity.getBase().getStatus().equals(EdgeLabel.PUBLISH.getString())) {
            return true;
        }

        return requesterId.equals(courseEntity.getAuthor().getUserId());
    }
}
