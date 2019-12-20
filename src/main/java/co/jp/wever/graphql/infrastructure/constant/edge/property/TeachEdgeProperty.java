package co.jp.wever.graphql.infrastructure.constant.edge.property;

public enum TeachEdgeProperty {
    LEVEL("level");

    private String value;

    TeachEdgeProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
