package co.jp.wever.graphql.domain.domainmodel.plan.element;

public enum PlanElementType {
    ARTICLE("article"), PLAN("plan");

    private String value;

    private PlanElementType(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }

    public static PlanElementType fromString(String value) {
        for (PlanElementType s : PlanElementType.values()) {
            if (s.value.equals(value)) {
                return s;
            }
        }
        return null;
    }
}
