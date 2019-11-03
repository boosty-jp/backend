package co.jp.wever.graphql.domain.service.plan;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.application.datamodel.request.Requester;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.converter.plan.PlanDetailConverter;
import co.jp.wever.graphql.domain.converter.plan.PlanListItemConverter;
import co.jp.wever.graphql.domain.domainmodel.plan.Plan;
import co.jp.wever.graphql.domain.domainmodel.plan.PlanDetail;
import co.jp.wever.graphql.domain.domainmodel.plan.PlanListItem;
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

    public PlanDetail findPlan(String planId, Requester requester) {

        PlanDetailEntity planDetailEntity = findPlanRepository.findOne(planId);

        PlanDetail planDetail = PlanDetailConverter.toPlanDetail(planDetailEntity);
        if (!planDetail.canRead(requester)) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        return planDetail;
    }

    public List<PlanListItem> findAllPlan(Requester requester) {

        List<PlanListItemEntity> planEntities = findPlanRepository.findAll(requester.getUserId());

        return planEntities.stream().map(p -> PlanListItemConverter.toPlanListItem(p)).collect(Collectors.toList());
    }

    public List<Plan> findAllPublishedPlan(Requester requester) {
        List<PlanEntity> planEntities = findPlanRepository.findAllPublished(requester.getUserId());
        return null;
    }

    public List<Plan> findAllDraftedPlan(Requester requester) {
        List<PlanEntity> planEntities = findPlanRepository.findAllDrafted(requester.getUserId());
        return null;
    }

    public List<Plan> findAllLikedPlan(Requester requester) {
        List<PlanEntity> planEntities = findPlanRepository.findAllLiked(requester.getUserId());
        return null;
    }

    public List<Plan> findAllLearnedPlan(Requester requester) {
        List<PlanEntity> planEntities = findPlanRepository.findAllLearned(requester.getUserId());
        return null;
    }

    public List<Plan> findAllLearningPlan(Requester requester) {
        List<PlanEntity> planEntities = findPlanRepository.findAllLearning(requester.getUserId());
        return null;
    }
}
