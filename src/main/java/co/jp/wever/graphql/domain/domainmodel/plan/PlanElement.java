package co.jp.wever.graphql.domain.domainmodel.plan;

import co.jp.wever.graphql.infrastructure.constant.PlanElementType;

public class PlanElement {
    private PlanNumber number;
    private PlanId id;
    private PlanElementType elementType;

    public PlanElement(PlanId id, PlanNumber number, PlanElementType elementType) {
        this.id = id;
        this.number = number;
        this.elementType = elementType;
    }
}
