package co.jp.wever.graphql.domain.converter.plan;

import co.jp.wever.graphql.domain.domainmodel.plan.element.FindPlanElement;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElementId;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElementImageUrl;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElementNumber;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElementTitle;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElementType;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanElementEntity;

public class FindPlanElementConverter {
    public static FindPlanElement toFindPlanElement(PlanElementEntity planElementEntity) {
        PlanElementId id = PlanElementId.of(planElementEntity.getId());
        PlanElementNumber number = PlanElementNumber.of(planElementEntity.getNumber());
        PlanElementTitle title = PlanElementTitle.of(planElementEntity.getTitle());
        PlanElementType type = PlanElementType.fromString(planElementEntity.getType());
        PlanElementImageUrl imageUrl = PlanElementImageUrl.of(planElementEntity.getImageUrl());

        return new FindPlanElement(id, number, title, type, imageUrl);
    }
}
