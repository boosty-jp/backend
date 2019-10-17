package co.jp.wever.graphql.domain.service.plan;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.converter.PlanConverter;
import co.jp.wever.graphql.domain.domainmodel.plan.Plan;
import co.jp.wever.graphql.domain.domainmodel.user.User;
import co.jp.wever.graphql.infrastructure.datamodel.PlanEntity;
import co.jp.wever.graphql.infrastructure.repository.plan.FindPlanRepositoryImpl;

@Service
public class FindPlanService {

    private final FindPlanRepositoryImpl findPlanRepository;

    FindPlanService(FindPlanRepositoryImpl findPlanRepository) {
        this.findPlanRepository = findPlanRepository;
    }

    public Plan findPlan(String planId, String userId) {
        PlanEntity planEntity = findPlanRepository.findOne(planId);

        Plan plan = PlanConverter.toPlan(planEntity);
        User user = User.of(userId);

        if (!plan.canReadUser(user)) {
            throw new IllegalArgumentException();
        }

        return plan;
    }

    public List<Plan> findAllPlan(String userId) {

        List<PlanEntity> planEntities = findPlanRepository.findAll(userId);
        return planEntities.stream().map(p -> PlanConverter.toPlan(p)).collect(Collectors.toList());
    }

    public List<Plan> findAllPublishedPlan(String userId) {
        List<PlanEntity> planEntities = findPlanRepository.findAllPublished(userId);
        return planEntities.stream().map(p -> PlanConverter.toPlan(p)).collect(Collectors.toList());
    }

    public List<Plan> findAllDraftedPlan(String userId) {
        List<PlanEntity> planEntities = findPlanRepository.findAllDrafted(userId);
        return planEntities.stream().map(p -> PlanConverter.toPlan(p)).collect(Collectors.toList());
    }

    public List<Plan> findAllLikedPlan(String userId) {
        List<PlanEntity> planEntities = findPlanRepository.findAllLiked(userId);
        return planEntities.stream().map(p -> PlanConverter.toPlan(p)).collect(Collectors.toList());
    }

    public List<Plan> findAllLearnedPlan(String userId) {
        List<PlanEntity> planEntities = findPlanRepository.findAllLearned(userId);
        return planEntities.stream().map(p -> PlanConverter.toPlan(p)).collect(Collectors.toList());
    }

    public List<Plan> findAllLearningPlan(String userId) {
        List<PlanEntity> planEntities = findPlanRepository.findAllLearning(userId);
        return planEntities.stream().map(p -> PlanConverter.toPlan(p)).collect(Collectors.toList());
    }
}
