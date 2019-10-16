package co.jp.wever.graphql.domain.service.plan;

import org.springframework.stereotype.Service;

import co.jp.wever.graphql.application.datamodel.request.PlanBaseInput;
import co.jp.wever.graphql.domain.converter.PlanBaseConverter;
import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanBase;
import co.jp.wever.graphql.domain.domainmodel.user.User;
import co.jp.wever.graphql.infrastructure.converter.entity.PlanBaseEntityConverter;
import co.jp.wever.graphql.infrastructure.datamodel.PlanBaseEntity;
import co.jp.wever.graphql.infrastructure.datamodel.PlanEntity;
import co.jp.wever.graphql.infrastructure.repository.PlanRepositoryImpl;

@Service
public class UpdatePlanService {

    private final PlanRepositoryImpl planRepositoryImpl;

    UpdatePlanService(PlanRepositoryImpl planRepositoryImpl) {
        this.planRepositoryImpl = planRepositoryImpl;
    }

    public void updatePlanBase(String planId, String userId, PlanBaseInput planBaseInput)
        throws IllegalAccessException {

        // 対象のユーザーのプランかチェック
        PlanEntity planEntity = planRepositoryImpl.findBase(planId);
        PlanBase targetPlanBase = PlanBaseConverter.toPlanBase(planEntity);

        User user = User.of(userId);

        if (!user.isSame(targetPlanBase.getAuthor())) {
            throw new IllegalAccessException();
        }

        PlanBase updatePlanBase = PlanBase.of(planBaseInput.getTitle(),
                                              planBaseInput.getDescription(),
                                              planBaseInput.getImageUrl(),
                                              planBaseInput.getTags(),
                                              userId,
                                              targetPlanBase.getStatus().name());
        PlanBaseEntity planBaseEntity = PlanBaseEntityConverter.toPlanBaseEntity(updatePlanBase);

        planRepositoryImpl.updateBase(planId, planBaseEntity);
    }


    public void publishPlan(String planId, String userId) throws IllegalAccessException {

        PlanEntity targetPlanEntity = this.planRepositoryImpl.findOne(planId);
        PlanBase planBase = PlanBaseConverter.toPlanBase(targetPlanEntity);

        User user = User.of(userId);
        if (!user.isSame(planBase.getAuthor())) {
            throw new IllegalAccessException();
        }

        // 公開できるかチェック
        // プランの基本チェック
        // エレメントのチェック
        this.planRepositoryImpl.publishOne(planId, userId);
    }

    public void draftPlan(String planId, String userId) throws IllegalAccessException {
        PlanEntity targetPlanEntity = this.planRepositoryImpl.findOne(planId);

        PlanBase planBase = PlanBaseConverter.toPlanBase(targetPlanEntity);
        User user = User.of(userId);
        if (!user.isSame(planBase.getAuthor())) {
            throw new IllegalAccessException();
        }

        // 下書き化できるかチェック
        // 対象のユーザーのプランかどうか
        this.planRepositoryImpl.draftOne(planId, userId);
    }

    public void startPlan(String planId, String userId) throws IllegalAccessException {
        if (!this.planRepositoryImpl.isPublishedPlan(planId)) {
            throw new IllegalAccessException();
        }
        this.planRepositoryImpl.startOne(planId, userId);
    }
}
