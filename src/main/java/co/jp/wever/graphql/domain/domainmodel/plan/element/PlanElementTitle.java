package co.jp.wever.graphql.domain.domainmodel.plan.element;

public class PlanElementTitle {
    private String value;

    private PlanElementTitle(String value) {
        this.value = value;
    }

    public static PlanElementTitle of(String value) {
        if (value.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return new PlanElementTitle(value);
    }

    public String getValue(){
        return value;
    }
}
