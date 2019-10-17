package co.jp.wever.graphql.domain.service.plan;

import org.springframework.stereotype.Service;

import java.util.List;

import co.jp.wever.graphql.application.datamodel.request.PlanBaseInput;
import co.jp.wever.graphql.application.datamodel.request.PlanElementInput;
import co.jp.wever.graphql.domain.converter.PlanConverter;
import co.jp.wever.graphql.domain.domainmodel.plan.Plan;
import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanBase;
import co.jp.wever.graphql.domain.domainmodel.user.User;
import co.jp.wever.graphql.infrastructure.converter.entity.PlanBaseEntityConverter;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanBaseEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanEntity;
import co.jp.wever.graphql.infrastructure.repository.plan.FindPlanRepositoryImpl;
import co.jp.wever.graphql.infrastructure.repository.plan.UpdatePlanRepositoryImpl;

@Service
public class UpdatePlanService {

    private final FindPlanRepositoryImpl findPlanRepository;
    private final UpdatePlanRepositoryImpl updatePlanRepository;

    UpdatePlanService(UpdatePlanRepositoryImpl updatePlanRepository, FindPlanRepositoryImpl findPlanRepository) {
        this.updatePlanRepository = updatePlanRepository;
        this.findPlanRepository = findPlanRepository;
    }

    public void updatePlanBase(String planId, String userId, PlanBaseInput planBaseInput)
        throws IllegalAccessException {

        // 対象のユーザーのプランかチェック
        PlanEntity planEntity = findPlanRepository.findOne(planId);
        Plan plan = PlanConverter.toPlan(planEntity);

        User user = User.of(userId);

        if (!user.isSame(plan.getAuthor())) {
            throw new IllegalAccessException();
        }

        PlanBase updatePlanBase = PlanBase.of(planBaseInput.getTitle(),
                                              planBaseInput.getDescription(),
                                              planBaseInput.getImageUrl(),
                                              planBaseInput.getTags(),
                                              userId,
                                              planEntity.getBaseEntity().getStatus());
        PlanBaseEntity planBaseEntity = PlanBaseEntityConverter.toPlanBaseEntity(updatePlanBase);

        updatePlanRepository.updateBase(planId, planBaseEntity);
    }

    public void updatePlanElements(String planId, String userId, List<PlanElementInput> elements)
        throws IllegalAccessException {

        // TODO: 基本的にcreateの方と同じなので、createができたのを使い回す
        // Repositoryの操作だけ違う
        // すでに作成したプランの連携を切らなければならない。
        updatePlanRepository.updateElements(planId, userId, null);
    }

    public void publishPlan(String planId, String userId) throws IllegalAccessException {

        PlanEntity targetPlanEntity = findPlanRepository.findOne(planId);
        Plan plan = PlanConverter.toPlan(targetPlanEntity);

        User user = User.of(userId);
        if (!user.isSame(plan.getAuthor())) {
            throw new IllegalAccessException();
        }

        // 公開できるかチェック
        // プランの基本チェック
        // エレメントのチェック
        updatePlanRepository.publishOne(planId, userId);
    }

    public void draftPlan(String planId, String userId) throws IllegalAccessException {
        PlanEntity targetPlanEntity = findPlanRepository.findOne(planId);

        Plan plan = PlanConverter.toPlan(targetPlanEntity);
        User user = User.of(userId);
        if (!user.isSame(plan.getAuthor())) {
            throw new IllegalAccessException();
        }

        // 下書き化できるかチェック
        // 対象のユーザーのプランかどうか
        this.updatePlanRepository.draftOne(planId, userId);
    }

    public void startPlan(String planId, String userId) throws IllegalAccessException {
        this.updatePlanRepository.startOne(planId, userId);
    }

    public void stopPlan(String planId, String userId) throws IllegalAccessException {
        this.updatePlanRepository.stopOne(planId, userId);
    }
}
