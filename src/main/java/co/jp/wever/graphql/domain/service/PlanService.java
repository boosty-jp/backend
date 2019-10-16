package co.jp.wever.graphql.domain.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.application.datamodel.request.PlanElementInput;
import co.jp.wever.graphql.domain.converter.PlanElementConverter;
import co.jp.wever.graphql.domain.domainmodel.plan.Plan;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElement;
import co.jp.wever.graphql.domain.converter.PlanConverter;
import co.jp.wever.graphql.domain.domainmodel.user.User;
import co.jp.wever.graphql.infrastructure.datamodel.PlanEntity;
import co.jp.wever.graphql.infrastructure.repository.PlanRepositoryImpl;

@Service
public class PlanService {

    private final PlanRepositoryImpl planRepositoryImpl;

    PlanService(PlanRepositoryImpl planRepositoryImpl) {
        this.planRepositoryImpl = planRepositoryImpl;
    }

    public Plan findPlan(String planId, String userId) {
        PlanEntity planEntity = this.planRepositoryImpl.findOne(planId);

        Plan plan = PlanConverter.toPlan(planEntity);
        User user = User.of(userId);

        // 下書き状態, 削除状態だった場合は、所有ユーザーだった場合のみ返却する
        if (!plan.isPublished() && !user.isSame(plan.getAuthor())) {
            throw new IllegalArgumentException();
        }

        return plan;
    }

    public List<Plan> findAllPlan(String userId) {

        List<PlanEntity> planEntities = this.planRepositoryImpl.findAll(userId);
        return planEntities.stream().map(p -> PlanConverter.toPlan(p)).collect(Collectors.toList());
    }

    public List<Plan> findAllPublishedPlan(String userId) {
        List<PlanEntity> planEntities = this.planRepositoryImpl.findAllPublished(userId);
        return planEntities.stream().map(p -> PlanConverter.toPlan(p)).collect(Collectors.toList());
    }

    public List<Plan> findAllDraftedPlan(String userId) {
        List<PlanEntity> planEntities = this.planRepositoryImpl.findAllDrafted(userId);
        return planEntities.stream().map(p -> PlanConverter.toPlan(p)).collect(Collectors.toList());
    }

    public List<Plan> findAllLikedPlan(String userId) {
        List<PlanEntity> planEntities = this.planRepositoryImpl.findAllLiked(userId);
        return planEntities.stream().map(p -> PlanConverter.toPlan(p)).collect(Collectors.toList());
    }

    public List<Plan> findAllLearnedPlan(String userId) {
        List<PlanEntity> planEntities = this.planRepositoryImpl.findAllLearned(userId);
        return planEntities.stream().map(p -> PlanConverter.toPlan(p)).collect(Collectors.toList());
    }

    public List<Plan> findAllLearningPlan(String userId) {
        List<PlanEntity> planEntities = this.planRepositoryImpl.findAllLearning(userId);
        return planEntities.stream().map(p -> PlanConverter.toPlan(p)).collect(Collectors.toList());
    }

    public String initPlan(String userId) {
        return this.planRepositoryImpl.initOne(userId);
    }

    public String addPlanElement(String planId, String userId, PlanElementInput elementInput) {

        // 対象のエレメント情報を取得
        // 公開されている(削除ずみ、下書き中ではない)エレメントデータかどうかチェック
        if (!this.planRepositoryImpl.isPublishedPlanElement(planId)) {
            // TODO: Exception検討する
            throw new IllegalArgumentException();
        }

        // 対象のプランを取得
        PlanEntity targetPlanEntity = this.planRepositoryImpl.findOne(planId);

        //ドメインモデルへの変換
        Plan plan = PlanConverter.toPlan(targetPlanEntity);

        // エレメントへ変換
        PlanElement planElement = PlanElementConverter.toPlanElement(elementInput);

        // 追加できるかチェック
        // 順番の不整合がないか
        // 追加できる数を超えていないか
        if (!plan.canAddElement(planElement)) {
            // TODO: Exception検討する
            throw new IllegalArgumentException();
        }

        // 追加する
        return this.planRepositoryImpl.addOne(planId, userId, null);
    }

    public void deletePlan(String planId, String userId) {
        // 削除できるかチェック
        this.planRepositoryImpl.deleteOne(planId, userId);
    }

    public void publishPlan(String planId, String userId) {
        // 公開できるかチェック
        // 対象のユーザーのプランかどうか
        // プランの基本チェック
        // エレメントのチェック
        this.planRepositoryImpl.publishOne(planId, userId);
    }

    public void draftPlan(String planId, String userId) {
        // 下書き化できるかチェック
        // 対象のユーザーのプランかどうか
        this.planRepositoryImpl.draftOne(planId, userId);
    }

    public void startPlan(String planId, String userId) {
        this.planRepositoryImpl.startOne(planId, userId);
    }
}
