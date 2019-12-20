package co.jp.wever.graphql.domain.service.course;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.application.datamodel.request.Requester;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.converter.plan.FindPlanElementDetailConverter;
import co.jp.wever.graphql.domain.converter.plan.PlanDetailConverter;
import co.jp.wever.graphql.domain.converter.plan.PlanListItemConverter;
import co.jp.wever.graphql.domain.domainmodel.plan.PlanDetail;
import co.jp.wever.graphql.domain.domainmodel.plan.PlanListItem;
import co.jp.wever.graphql.domain.domainmodel.plan.element.FindPlanElementDetail;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import co.jp.wever.graphql.infrastructure.datamodel.course.LearningPlanItemEntity;
import co.jp.wever.graphql.infrastructure.datamodel.course.aggregation.PlanDetailEntity;
import co.jp.wever.graphql.infrastructure.datamodel.course.aggregation.PlanItemEntity;
import co.jp.wever.graphql.infrastructure.datamodel.course.aggregation.PlanListItemEntity;
import co.jp.wever.graphql.infrastructure.repository.course.FindPlanRepositoryImpl;

@Service
public class FindCourseService {

    private final FindPlanRepositoryImpl findPlanRepository;

    FindCourseService(FindPlanRepositoryImpl findPlanRepository) {
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
        PlanDetailEntity planDetailEntity;
        if (requester.isGuest()) {
            planDetailEntity = findPlanRepository.findDetailForGuest(planId);
        } else {
            planDetailEntity = findPlanRepository.findDetail(planId, requester.getUserId());
        }

        PlanDetail planDetail = PlanDetailConverter.toPlanDetail(planDetailEntity);
        if (!planDetail.canRead(requester)) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        return planDetail;
    }

    public List<PlanListItem> findAllPlan(Requester requester) {
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        List<PlanListItemEntity> planEntities = findPlanRepository.findAll(requester.getUserId());

        return planEntities.stream().map(p -> PlanListItemConverter.toPlanListItem(p)).collect(Collectors.toList());
    }

    public List<PlanListItem> findAllPublishedPlan(String userId) {
        List<PlanListItemEntity> planEntities = findPlanRepository.findAllPublished(userId);
        return planEntities.stream().map(p -> PlanListItemConverter.toPlanListItem(p)).collect(Collectors.toList());
    }

    public List<PlanItemEntity> findAllLikedPlan(Requester requester) {
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.NEED_LOGIN.getString());
        }
        return findPlanRepository.findAllLiked(requester.getUserId());
    }

    public List<LearningPlanItemEntity> findAllLearnedPlan(Requester requester) {
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.NEED_LOGIN.getString());
        }
        return findPlanRepository.findAllLearning(requester.getUserId());
    }

    public List<LearningPlanItemEntity> findAllLearningPlan(Requester requester) {
        //TODO: Entity返すのはやめる
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.NEED_LOGIN.getString());
        }
        return findPlanRepository.findAllLearning(requester.getUserId());
    }

    public List<FindPlanElementDetail> findAllPlanElementDetails(String planId, Requester requester) {
        if (requester.isGuest()) {
            // ゲスト用のクエリ
            return findPlanRepository.findAllPlanElementDetailsForGuest(planId)
                                     .stream()
                                     .map(p -> FindPlanElementDetailConverter.toFindPlanElementDetail(p))
                                     .collect(Collectors.toList());
        }

        return findPlanRepository.findAllPlanElementDetails(planId, requester.getUserId())
                                 .stream()
                                 .map(p -> FindPlanElementDetailConverter.toFindPlanElementDetail(p))
                                 .collect(Collectors.toList());
    }

    public List<PlanItemEntity> findFamous() {
        return findPlanRepository.findFamous();
    }
}
