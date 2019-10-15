package co.jp.wever.graphql.infrastructure.constant;

public enum PlanElementType {
    ARTICLE("article"), PLAN("plan");

    private String value;

    private PlanElementType(String value) {
        this.value = value;
    }
}
