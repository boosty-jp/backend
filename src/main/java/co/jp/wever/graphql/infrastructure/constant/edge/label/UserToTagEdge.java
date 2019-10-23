package co.jp.wever.graphql.infrastructure.constant.edge.label;

public enum UserToTagEdge {
    RELATED("related");

    private String value;

    private UserToTagEdge(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
