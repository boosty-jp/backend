package co.jp.wever.graphql.infrastructure.constant.edge.label;

public enum PlanToPlanElementEdge {
    INCLUDE("include");

    private String value;

    private PlanToPlanElementEdge(String value) {
        this.value = value;
    }
}
