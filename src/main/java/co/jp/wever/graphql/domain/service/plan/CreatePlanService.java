package co.jp.wever.graphql.domain.service.plan;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.application.datamodel.request.PlanBaseInput;
import co.jp.wever.graphql.application.datamodel.request.PlanElementInput;
import co.jp.wever.graphql.domain.converter.PlanBaseConverter;
import co.jp.wever.graphql.domain.converter.PlanElementConverter;
import co.jp.wever.graphql.domain.domainmodel.plan.Plan;
import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanBase;
import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanStatus;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElement;
import co.jp.wever.graphql.domain.domainmodel.user.User;
import co.jp.wever.graphql.infrastructure.converter.entity.PlanBaseEntityConverter;
import co.jp.wever.graphql.infrastructure.datamodel.PlanBaseEntity;
import co.jp.wever.graphql.infrastructure.datamodel.PlanEntity;
import co.jp.wever.graphql.infrastructure.repository.PlanRepositoryImpl;

@Service
public class CreatePlanService {
    private final PlanRepositoryImpl planRepositoryImpl;

    CreatePlanService(PlanRepositoryImpl planRepositoryImpl) {
        this.planRepositoryImpl = planRepositoryImpl;
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

}
