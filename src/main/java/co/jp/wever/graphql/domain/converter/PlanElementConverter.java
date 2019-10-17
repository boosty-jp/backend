package co.jp.wever.graphql.domain.converter;

import co.jp.wever.graphql.application.datamodel.request.PlanElementInput;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElement;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElementId;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElementNumber;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElementTitle;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElementType;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanElementEntity;

public class PlanElementConverter {
    public static PlanElement toPlanElement(PlanElementInput planElementInput) {
        return new PlanElement(PlanElementId.of(planElementInput.getTargetId()),
                               PlanElementTitle.of(planElementInput.getTitle()),
                               PlanElementNumber.of(planElementInput.getNumber()),
                               PlanElementType.valueOf(planElementInput.getType()));
    }

    public static PlanElement toPlanElement(PlanElementEntity planElementEntity) {
        return new PlanElement(PlanElementId.of(planElementEntity.getTargetId()),
                               PlanElementTitle.of(planElementEntity.getTitle()),
                               PlanElementNumber.of(planElementEntity.getNumber()),
                               PlanElementType.valueOf(planElementEntity.getType().name()));
    }
}
