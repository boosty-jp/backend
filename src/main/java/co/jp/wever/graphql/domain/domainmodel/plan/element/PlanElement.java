package co.jp.wever.graphql.domain.domainmodel.plan.element;

public class PlanElement {

    private PlanElementId id;
    private PlanElementNumber number;

    public PlanElement(
        PlanElementId id, PlanElementNumber number) {
        this.id = id;
        this.number = number;
    }

    public String getId() {
        return id.getValue();
    }

    public int getNumber() {
        return number.getValue();
    }

}
