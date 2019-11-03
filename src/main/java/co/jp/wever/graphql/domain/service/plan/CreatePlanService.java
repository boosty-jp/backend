package co.jp.wever.graphql.domain.service.plan;

import org.springframework.stereotype.Service;

import java.util.List;

import co.jp.wever.graphql.application.datamodel.request.PlanBaseInput;
import co.jp.wever.graphql.application.datamodel.request.PlanElementInput;
import co.jp.wever.graphql.application.datamodel.request.Requester;
import co.jp.wever.graphql.infrastructure.repository.plan.CreatePlanRepositoryImpl;
import co.jp.wever.graphql.infrastructure.repository.plan.FindPlanRepositoryImpl;

@Service
public class CreatePlanService {
    private final CreatePlanRepositoryImpl createPlanRepository;
    private final FindPlanRepositoryImpl findPlanRepository;

    CreatePlanService(CreatePlanRepositoryImpl createPlanRepository, FindPlanRepositoryImpl findPlanRepository) {
        this.createPlanRepository = createPlanRepository;
        this.findPlanRepository = findPlanRepository;
    }

    public String initPlan(Requester requester) {
        return createPlanRepository.initOne(requester.getUserId());
    }

    public String createPlanBase(String userId, PlanBaseInput planBaseInput) {
        //        PlanBase planBase = PlanBase.of(planBaseInput.getTitle(),
        //                                        planBaseInput.getDescription(),
        //                                        planBaseInput.getImageUrl(),
        //                                        planBaseInput.getTags(),
        //                                        userId,
        //                                        PlanStatus.DRAFTED.getString());
        //
        //        PlanBaseEntity planBaseEntity = PlanBaseEntityConverter.toPlanBaseEntity(planBase);
        //        return createPlanRepository.createBase(userId, planBaseEntity);
        return "";
    }

    public void createPlanElements(String planId, String userId, List<PlanElementInput> elements) {
        //
        //        List<String> targetIds = elements.stream().map(e -> e.getTargetId()).collect(Collectors.toList());
        //        // 追加するエレメントが公開されているかチェック
        //        List<String> publishedPlanElementIds = findPlanRepository.findPublishedPlanElementIds(targetIds);
        //
        //        // TODO: ドメインサービスに移す
        //        if (!publishedPlanElementIds.stream().allMatch(id -> targetIds.contains(id))) {
        //            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
        //                                             GraphQLErrorMessage.INVALID_PLAN_ELEMENT_ID.getString());
        //        }
        //
        //        if (!targetIds.stream().allMatch(id -> publishedPlanElementIds.contains(id))) {
        //            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
        //                                             GraphQLErrorMessage.INVALID_PLAN_ELEMENT_ID.getString());
        //        }
        //
        //        PlanEntity targetPlanEntity = findPlanRepository.findOne(planId);
        //
        //        Plan plan = PlanConverter.toPlan(targetPlanEntity);
        //
        //        List<PlanElement> planElements =
        //            elements.stream().map(e -> PlanElementConverter.toPlanElement(e)).collect(Collectors.toList());
        //
        //        UserId creatorId = UserId.of(userId);
        //        if (!creatorId.same(plan.getAuthorId())) {
        //            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
        //                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        //        }
        //
        //        createPlanRepository.createElements(planId, null);
    }

}
