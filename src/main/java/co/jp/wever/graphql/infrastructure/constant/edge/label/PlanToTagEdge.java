package co.jp.wever.graphql.infrastructure.constant.edge.label;

public enum PlanToTagEdge {
    RELATED("related");

    private String value;

    private PlanToTagEdge(String value) {
        this.value = value;
    }
}
