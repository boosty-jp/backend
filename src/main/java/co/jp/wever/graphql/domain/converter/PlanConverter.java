package co.jp.wever.graphql.domain.converter;

import co.jp.wever.graphql.domain.domainmodel.plan.Plan;
import co.jp.wever.graphql.domain.domainmodel.plan.PlanDescription;
import co.jp.wever.graphql.domain.domainmodel.plan.PlanId;
import co.jp.wever.graphql.domain.domainmodel.plan.PlanTitle;
import co.jp.wever.graphql.infrastructure.datamodel.PlanEntity;

public class PlanConverter {
    public static Plan toPlan(PlanEntity planEntity){
        PlanId planId = PlanId.of(planEntity.getId());
        PlanTitle planTitle = PlanTitle.of(planEntity.getTitle());
        PlanDescription planDescription = PlanDescription.of(planEntity.getDescription());
        String imageUrl =planEntity.getImage();

        //TODO: null修正
        return new Plan(planId, planTitle, planDescription, imageUrl,null);
    }
}
