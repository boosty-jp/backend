package co.jp.wever.graphql.domain.service.plan;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.application.datamodel.request.PlanBaseInput;
import co.jp.wever.graphql.application.datamodel.request.PlanElementInput;
import co.jp.wever.graphql.domain.converter.plan.PlanConverter;
import co.jp.wever.graphql.domain.converter.plan.PlanElementConverter;
import co.jp.wever.graphql.domain.domainmodel.plan.Plan;
import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanBase;
import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanStatus;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElement;
import co.jp.wever.graphql.domain.domainmodel.user.User;
import co.jp.wever.graphql.infrastructure.converter.entity.plan.PlanBaseEntityConverter;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanBaseEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanEntity;
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

    public String createPlanBase(String userId, PlanBaseInput planBaseInput) {
        PlanBase planBase = PlanBase.of(planBaseInput.getTitle(),
                                        planBaseInput.getDescription(),
                                        planBaseInput.getImageUrl(),
                                        planBaseInput.getTags(),
                                        userId,
                                        PlanStatus.DRAFTED.name());

        PlanBaseEntity planBaseEntity = PlanBaseEntityConverter.toPlanBaseEntity(planBase);
        return createPlanRepository.createBase(userId, planBaseEntity);
    }

    public void createPlanElements(String planId, String userId, List<PlanElementInput> elements)
        throws IllegalAccessException {

        List<String> targetIds = elements.stream().map(e -> e.getTargetId()).collect(Collectors.toList());
        // 追加するエレメントが公開されているかチェック
        List<String> publishedPlanElementIds = findPlanRepository.findPublishedPlanElementIds(targetIds);

        // TODO: ドメインサービスに移す
        if (!publishedPlanElementIds.stream().allMatch(id -> targetIds.contains(id))) {
            throw new IllegalArgumentException();
        }

        if (!targetIds.stream().allMatch(id -> publishedPlanElementIds.contains(id))) {
            throw new IllegalArgumentException();
        }

        PlanEntity targetPlanEntity = findPlanRepository.findOne(planId);

        Plan plan = PlanConverter.toPlan(targetPlanEntity);

        List<PlanElement> planElements =
            elements.stream().map(e -> PlanElementConverter.toPlanElement(e)).collect(Collectors.toList());

        User user = User.of(userId);
        if (!user.isSame(plan.getAuthor())) {
            throw new IllegalAccessException();
        }

        // 作成できるかプランエレメントかどうかチェック
        // 順番の不整合がないか
        // 追加できる数を超えていないか
        //        if (!plan.canCreateElement(planElement)) {
        //            // TODO: Exception検討する
        //            throw new IllegalArgumentException();
        //        }

        createPlanRepository.createElements(planId, null);
    }

}
