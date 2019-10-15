package co.jp.wever.graphql.domain.factory;

import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElement;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElementId;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElementNumber;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElementType;

public class PlanElementFactory {

    public static PlanElement makePlanElement(String id, int number, String elementType) {

        PlanElementNumber planElementNumber = PlanElementNumber.of(number);
        PlanElementId planElementId = PlanElementId.of(id);
        PlanElementType planElementType = PlanElementType.valueOf(elementType);

        return new PlanElement(planElementId, planElementNumber, planElementType);
    }

//    public static PlanElement makePlanElement(Object elementObject) {
//
//        PlanNumber planNumber = PlanNumber.of(elementObject.ke);
//        PlanId planId = PlanId.of(id);
//        PlanElementType planElementType = PlanElementType.valueOf(elementType);
//
//        return new PlanElement(planId, planNumber, planElementType);
//    }

}
