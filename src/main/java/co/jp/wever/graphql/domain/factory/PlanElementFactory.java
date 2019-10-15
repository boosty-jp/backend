package co.jp.wever.graphql.domain.factory;

import co.jp.wever.graphql.domain.domainmodel.plan.PlanElement;
import co.jp.wever.graphql.domain.domainmodel.plan.PlanId;
import co.jp.wever.graphql.domain.domainmodel.plan.PlanNumber;
import co.jp.wever.graphql.infrastructure.constant.PlanElementType;

public class PlanElementFactory {

    public static PlanElement makePlanElement(String id, int number, String elementType) {

        PlanNumber planNumber = PlanNumber.of(number);
        PlanId planId = PlanId.of(id);
        PlanElementType planElementType = PlanElementType.valueOf(elementType);

        return new PlanElement(planId, planNumber, planElementType);
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
