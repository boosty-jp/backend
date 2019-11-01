package co.jp.wever.graphql.domain.service.plan;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.converter.plan.PlanConverter;
import co.jp.wever.graphql.domain.converter.plan.PlanDetailConverter;
import co.jp.wever.graphql.domain.converter.plan.PlanListItemConverter;
import co.jp.wever.graphql.domain.domainmodel.plan.Plan;
import co.jp.wever.graphql.domain.domainmodel.plan.PlanDetail;
import co.jp.wever.graphql.domain.domainmodel.plan.PlanListItem;
import co.jp.wever.graphql.domain.domainmodel.user.UserId;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.aggregation.PlanDetailEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.aggregation.PlanListItemEntity;
import co.jp.wever.graphql.infrastructure.repository.plan.FindPlanRepositoryImpl;

@Service
public class FindPlanService {

    private final FindPlanRepositoryImpl findPlanRepository;

    FindPlanService(FindPlanRepositoryImpl findPlanRepository) {
        this.findPlanRepository = findPlanRepository;
    }

    public PlanDetail findPlan(String planId, String userId) {
        PlanDetailEntity planDetailEntity = findPlanRepository.findOne(planId);

        PlanDetail planDetail = PlanDetailConverter.toPlanDetail(planDetailEntity);
        if (!planDetail.canRead(UserId.of(userId))) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        return planDetail;
    }

    public List<PlanListItem> findAllPlan(String userId) {

        List<PlanListItemEntity> planEntities = findPlanRepository.findAll(userId);
        return planEntities.stream().map(p -> PlanListItemConverter.toPlanListItem(p)).collect(Collectors.toList());
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
