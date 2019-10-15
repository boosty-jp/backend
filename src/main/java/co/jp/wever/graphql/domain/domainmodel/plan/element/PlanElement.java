package co.jp.wever.graphql.domain.domainmodel.plan.element;

public class PlanElement {
    private PlanElementId id;
    private PlanElementNumber number;
    private PlanElementType elementType;

    public PlanElement(PlanElementId id, PlanElementNumber number, PlanElementType elementType) {
        this.id = id;
        this.number = number;
        this.elementType = elementType;
    }

    public PlanElementNumber getNumber() {
        return number;
    }
}
