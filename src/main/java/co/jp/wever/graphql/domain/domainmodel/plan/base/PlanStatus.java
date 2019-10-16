package co.jp.wever.graphql.domain.domainmodel.plan.base;

public enum PlanStatus {
    PUBLISHED("published"), DRAFTED("drafted"), DELETED("deleted");

    private String value;

    private PlanStatus(String value) {
        this.value = value;
    }
}
