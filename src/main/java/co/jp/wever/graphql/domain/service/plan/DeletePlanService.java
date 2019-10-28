package co.jp.wever.graphql.domain.service.plan;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.converter.plan.PlanBaseConverter;
import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanBase;
import co.jp.wever.graphql.domain.domainmodel.user.UserId;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanBaseEntity;
import co.jp.wever.graphql.infrastructure.repository.plan.DeletePlanRepositoryImpl;
import co.jp.wever.graphql.infrastructure.repository.plan.FindPlanRepositoryImpl;

@Service
public class DeletePlanService {

    private final DeletePlanRepositoryImpl deletePlanRepository;
    private final FindPlanRepositoryImpl findPlanRepository;

    DeletePlanService(DeletePlanRepositoryImpl deletePlanRepository, FindPlanRepositoryImpl findPlanRepository) {
        this.deletePlanRepository = deletePlanRepository;
        this.findPlanRepository = findPlanRepository;
    }

    public void deletePlan(String planId, String userId) {

        PlanBaseEntity planBaseEntity = findPlanRepository.findBase(planId);
        PlanBase planBase = PlanBaseConverter.toPlanBase(planBaseEntity);

        UserId deleterId = UserId.of(userId);
        if (!deleterId.same(planBase.getAuthorId())) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        // 削除できるかチェック
        deletePlanRepository.deleteOne(planId, userId);
    }


}
