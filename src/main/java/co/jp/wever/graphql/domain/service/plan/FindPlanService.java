package co.jp.wever.graphql.domain.service.plan;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.application.datamodel.request.Requester;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.converter.plan.FindPlanElementDetailConverter;
import co.jp.wever.graphql.domain.converter.plan.PlanDetailConverter;
import co.jp.wever.graphql.domain.converter.plan.PlanListItemConverter;
import co.jp.wever.graphql.domain.domainmodel.plan.Plan;
import co.jp.wever.graphql.domain.domainmodel.plan.PlanDetail;
import co.jp.wever.graphql.domain.domainmodel.plan.PlanListItem;
import co.jp.wever.graphql.domain.domainmodel.plan.element.FindPlanElementDetail;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import co.jp.wever.graphql.infrastructure.datamodel.plan.LearningPlanItemEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.aggregation.FamousPlanEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.aggregation.PlanDetailEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.aggregation.PlanListItemEntity;
import co.jp.wever.graphql.infrastructure.repository.plan.FindPlanRepositoryImpl;

@Service
public class FindPlanService {

    private final FindPlanRepositoryImpl findPlanRepository;

    FindPlanService(FindPlanRepositoryImpl findPlanRepository) {
        this.findPlanRepository = findPlanRepository;
    }

    public PlanListItem findOne(String planId, Requester requester) {

        PlanListItemEntity planListItemEntity = findPlanRepository.findOne(planId, requester.getUserId());

        PlanListItem planListItem = PlanListItemConverter.toPlanListItem(planListItemEntity);

        if (!planListItem.published()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        return planListItem;
    }

    public PlanDetail findDetail(String planId, Requester requester) {

        PlanDetailEntity planDetailEntity = findPlanRepository.findDetail(planId);

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

    public List<PlanListItem> findAllPublishedPlan(String userId) {
        List<PlanListItemEntity> planEntities = findPlanRepository.findAllPublished(userId);
        return planEntities.stream().map(p -> PlanListItemConverter.toPlanListItem(p)).collect(Collectors.toList());
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

    public List<LearningPlanItemEntity> findAllLearningPlan(String userId) {
        //TODO: Entity返すのはやめる
        return findPlanRepository.findAllLearning(userId);
    }

    public List<FindPlanElementDetail> findAllPlanElementDetails(String planId, Requester requester) {

        return findPlanRepository.findAllPlanElementDetails(planId, requester.getUserId())
                                 .stream()
                                 .map(p -> FindPlanElementDetailConverter.toFindPlanElementDetail(p))
                                 .collect(Collectors.toList());
    }

    public List<FamousPlanEntity> findFamous() {
        return findPlanRepository.findFamous();
    }
}
