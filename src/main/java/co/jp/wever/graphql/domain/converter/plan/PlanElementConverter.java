package co.jp.wever.graphql.domain.converter.plan;

import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElement;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElementId;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElementNumber;

public class PlanElementConverter {
    public static PlanElement toPlanElement(PlanElementInput planElementInput) {
        return new PlanElement(PlanElementId.of(planElementInput.getId()),
                               PlanElementNumber.of(planElementInput.getNumber()));
    }
}
