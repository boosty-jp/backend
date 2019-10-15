package co.jp.wever.graphql.domain.converter;

import co.jp.wever.graphql.application.datamodel.request.PlanElementInput;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElement;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElementId;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElementNumber;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElementType;

public class PlanElementConverter {
    public static PlanElement toPlanElement(PlanElementInput planElementInput) {
        return new PlanElement(PlanElementId.of(planElementInput.getTargetId()), PlanElementNumber.of(planElementInput.getNumber()), PlanElementType.valueOf(planElementInput.getType()));
    }
}
