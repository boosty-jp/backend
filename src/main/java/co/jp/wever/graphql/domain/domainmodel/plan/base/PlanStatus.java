package co.jp.wever.graphql.domain.domainmodel.plan.base;

public enum PlanStatus {
    PUBLISHED("published"), DRAFTED("drafted"), DELETED("deleted");

    private String value;

    private PlanStatus(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }

    public static PlanStatus fromString(String value) {
        for (PlanStatus s : PlanStatus.values()) {
            if (s.value.equalsIgnoreCase(value)) {
                return s;
            }
        }
        return null;
    }
}
