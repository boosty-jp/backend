package co.jp.wever.graphql.domain.factory;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.domainmodel.plan.Plan;
import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanDescription;
import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanTitle;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElement;
import co.jp.wever.graphql.infrastructure.datamodel.PlanElementEntity;
import co.jp.wever.graphql.infrastructure.datamodel.PlanEntity;

public class PlanFactory {
    public static Plan makePlan(PlanEntity planEntity, List<PlanElementEntity> elements) {
        PlanId planId = PlanId.of(planEntity.getId());
        PlanTitle planTitle = PlanTitle.of(planEntity.getTitle());
        PlanDescription planDescription = PlanDescription.of(planEntity.getDescription());
        String imageUrl = planEntity.getImage();

        List<PlanElement> planElements = elements.stream().map(e -> PlanElementFactory.makePlanElement(e)).collect(Collectors.toList());

        return new Plan(planId, planTitle, planDescription, imageUrl, planElements);
    }
}
