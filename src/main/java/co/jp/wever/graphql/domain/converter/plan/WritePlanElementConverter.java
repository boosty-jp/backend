package co.jp.wever.graphql.domain.converter.plan;

import co.jp.wever.graphql.domain.domainmodel.plan.element.WritePlanElement;

public class WritePlanElementConverter {
    public static WritePlanElement toWritePlanElement(PlanElementInput planElementInput){
        return WritePlanElement.of(planElementInput.getId(), planElementInput.getNumber());
    }
}
