package co.jp.wever.graphql.domain.service.plan;

import org.springframework.stereotype.Service;

import co.jp.wever.graphql.domain.converter.PlanBaseConverter;
import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanBase;
import co.jp.wever.graphql.domain.domainmodel.user.User;
import co.jp.wever.graphql.infrastructure.datamodel.PlanBaseEntity;
import co.jp.wever.graphql.infrastructure.repository.PlanRepositoryImpl;

@Service
public class DeletePlanService {

    private final PlanRepositoryImpl planRepositoryImpl;

    DeletePlanService(PlanRepositoryImpl planRepositoryImpl) {
        this.planRepositoryImpl = planRepositoryImpl;
    }

    public void deletePlan(String planId, String userId) throws IllegalAccessException {

        PlanBaseEntity planBaseEntity = this.planRepositoryImpl.findBase(planId);
        PlanBase planBase = PlanBaseConverter.toPlanBase(planBaseEntity);

        User user = User.of(userId);
        if (!user.isSame(planBase.getAuthor())) {
            throw new IllegalAccessException();
        }

        // 削除できるかチェック
        this.planRepositoryImpl.deleteOne(planId, userId);
    }


}
