package co.jp.wever.graphql.domain.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.application.datamodel.request.PlanBaseInput;
import co.jp.wever.graphql.application.datamodel.request.PlanElementInput;
import co.jp.wever.graphql.domain.converter.PlanElementConverter;
import co.jp.wever.graphql.domain.domainmodel.plan.Plan;
import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanBase;
import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanStatus;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElement;
import co.jp.wever.graphql.domain.converter.PlanBaseConverter;
import co.jp.wever.graphql.domain.domainmodel.user.User;
import co.jp.wever.graphql.infrastructure.converter.entity.PlanBaseEntityConverter;
import co.jp.wever.graphql.infrastructure.datamodel.PlanBaseEntity;
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

        PlanBase planBase = PlanBaseConverter.toPlanBase(planEntity);
        User user = User.of(userId);

        // 下書き状態, 削除状態だった場合は、所有ユーザーだった場合のみ返却する
        if (!planBase.isPublished() && !user.isSame(planBase.getAuthor())) {
            throw new IllegalArgumentException();
        }

        return planBase;
    }

    public List<PlanBase> findAllPlan(String userId) {

        List<PlanEntity> planEntities = this.planRepositoryImpl.findAll(userId);
        return planEntities.stream().map(p -> PlanBaseConverter.toPlanBase(p)).collect(Collectors.toList());
    }

    public List<PlanBase> findAllPublishedPlan(String userId) {
        List<PlanEntity> planEntities = this.planRepositoryImpl.findAllPublished(userId);
        return planEntities.stream().map(p -> PlanBaseConverter.toPlanBase(p)).collect(Collectors.toList());
    }

    public List<PlanBase> findAllDraftedPlan(String userId) {
        List<PlanEntity> planEntities = this.planRepositoryImpl.findAllDrafted(userId);
        return planEntities.stream().map(p -> PlanBaseConverter.toPlanBase(p)).collect(Collectors.toList());
    }

    public List<PlanBase> findAllLikedPlan(String userId) {
        List<PlanEntity> planEntities = this.planRepositoryImpl.findAllLiked(userId);
        return planEntities.stream().map(p -> PlanBaseConverter.toPlanBase(p)).collect(Collectors.toList());
    }

    public List<PlanBase> findAllLearnedPlan(String userId) {
        List<PlanEntity> planEntities = this.planRepositoryImpl.findAllLearned(userId);
        return planEntities.stream().map(p -> PlanBaseConverter.toPlanBase(p)).collect(Collectors.toList());
    }

    public List<PlanBase> findAllLearningPlan(String userId) {
        List<PlanEntity> planEntities = this.planRepositoryImpl.findAllLearning(userId);
        return planEntities.stream().map(p -> PlanBaseConverter.toPlanBase(p)).collect(Collectors.toList());
    }

    public String createPlanBase(String userId, PlanBaseInput planBaseInput) {
        PlanBase planBase = PlanBase.of(planBaseInput.getTitle(),
                                        planBaseInput.getDescription(),
                                        planBaseInput.getImageUrl(),
                                        planBaseInput.getTags()
                                        userId,
                                        PlanStatus.DRAFTED.name());

        PlanBaseEntity planBaseEntity = PlanBaseEntityConverter.toPlanBaseEntity(planBase);
        return planRepositoryImpl.createBase(userId, planBaseEntity);
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

    public void createPlanElements(String planId, String userId, List<PlanElementInput> elementsInput)
        throws IllegalAccessException {

        List<String> targetIds = elementsInput.stream().map(e -> e.getTargetId()).collect(Collectors.toList());
        // 追加するエレメントが公開されているかチェック
        List<String> publishedPlanElementIds = this.planRepositoryImpl.findPublishedPlanElementIds(targetIds);

        // TODO: ドメインサービスに移す
        if (!publishedPlanElementIds.stream().allMatch(id -> targetIds.contains(id))) {
            throw new IllegalArgumentException();
        }

        if (!targetIds.stream().allMatch(id -> publishedPlanElementIds.contains(id))) {
            throw new IllegalArgumentException();
        }

        PlanEntity targetPlanEntity = this.planRepositoryImpl.findBase(planId);

        PlanBase planBase = PlanBaseConverter.toPlanBase(targetPlanEntity);

        List<PlanElement> planElements =
            elementsInput.stream().map(e -> PlanElementConverter.toPlanElement(e)).collect(Collectors.toList());

        User user = User.of(userId);
        if (!user.isSame(planBase.getAuthor())) {
            throw new IllegalAccessException();
        }

        Plan plan = new Plan(planBase, planElements);

        // 作成できるかプランエレメントかどうかチェック
        // 順番の不整合がないか
        // 追加できる数を超えていないか
        if (!plan.canCreateElement(planElement)) {
            // TODO: Exception検討する
            throw new IllegalArgumentException();
        }

        this.planRepositoryImpl.createElements(planId, null);
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
