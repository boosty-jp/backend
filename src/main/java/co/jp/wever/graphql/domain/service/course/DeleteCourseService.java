package co.jp.wever.graphql.domain.service.course;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import co.jp.wever.graphql.application.datamodel.request.Requester;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.domainmodel.user.UserId;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import co.jp.wever.graphql.infrastructure.repository.plan.DeletePlanRepositoryImpl;
import co.jp.wever.graphql.infrastructure.repository.plan.FindPlanRepositoryImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DeleteCourseService {

    private final DeletePlanRepositoryImpl deletePlanRepository;
    private final FindPlanRepositoryImpl findPlanRepository;

    DeleteCourseService(DeletePlanRepositoryImpl deletePlanRepository, FindPlanRepositoryImpl findPlanRepository) {
        this.deletePlanRepository = deletePlanRepository;
        this.findPlanRepository = findPlanRepository;
    }

    public void deletePlan(String planId, Requester requester) {
        log.info("delete planId: {}", planId);

        UserId authorId = UserId.of(findPlanRepository.findAuthorId(planId));
        UserId deleterId = UserId.of(requester.getUserId());

        if (!deleterId.same(authorId)) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        deletePlanRepository.deleteOne(planId, requester.getUserId());
    }
}
