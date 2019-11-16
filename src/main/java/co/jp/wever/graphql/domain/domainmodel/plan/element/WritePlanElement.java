package co.jp.wever.graphql.domain.domainmodel.plan.element;

public class WritePlanElement {

    private PlanElementId id;
    private PlanElementNumber number;

    private WritePlanElement(PlanElementId id, PlanElementNumber number) {
        this.id = id;
        this.number = number;
    }

    public static WritePlanElement of(String id, int number) {
        PlanElementId planElementId = PlanElementId.of(id);
        PlanElementNumber planElementNumber = PlanElementNumber.of(number);

        return new WritePlanElement(planElementId, planElementNumber);
    }

    public int getNumber() {
        return number.getValue();
    }

    public String getId() {
        return id.getValue();
    }

}
