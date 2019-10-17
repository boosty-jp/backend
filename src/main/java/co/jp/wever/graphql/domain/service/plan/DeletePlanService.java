package co.jp.wever.graphql.domain.service.plan;

import org.springframework.stereotype.Service;

import co.jp.wever.graphql.domain.converter.PlanBaseConverter;
import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanBase;
import co.jp.wever.graphql.domain.domainmodel.user.User;
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

    public void deletePlan(String planId, String userId) throws IllegalAccessException {

        PlanBaseEntity planBaseEntity = findPlanRepository.findBase(planId);
        PlanBase planBase = PlanBaseConverter.toPlanBase(planBaseEntity);

        User user = User.of(userId);
        if (!user.isSame(planBase.getAuthor())) {
            throw new IllegalAccessException();
        }

        // 削除できるかチェック
        deletePlanRepository.deleteOne(planId, userId);
    }


}
