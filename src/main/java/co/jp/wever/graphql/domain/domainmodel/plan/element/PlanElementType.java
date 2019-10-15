package co.jp.wever.graphql.domain.domainmodel.plan.element;

public enum PlanElementType {
    ARTICLE("article"), PLAN("plan");

    private String value;

    private PlanElementType(String value) {
        this.value = value;
    }
}
