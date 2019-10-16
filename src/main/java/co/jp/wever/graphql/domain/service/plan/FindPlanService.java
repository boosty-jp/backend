package co.jp.wever.graphql.domain.service.plan;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.converter.PlanBaseConverter;
import co.jp.wever.graphql.domain.domainmodel.plan.Plan;
import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanBase;
import co.jp.wever.graphql.domain.domainmodel.user.User;
import co.jp.wever.graphql.infrastructure.datamodel.PlanEntity;
import co.jp.wever.graphql.infrastructure.repository.PlanRepositoryImpl;

@Service
public class FindPlanService {

    private final PlanRepositoryImpl planRepositoryImpl;

    FindPlanService(PlanRepositoryImpl planRepositoryImpl) {
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
}
