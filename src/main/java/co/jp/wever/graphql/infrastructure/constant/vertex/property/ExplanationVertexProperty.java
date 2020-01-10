package co.jp.wever.graphql.infrastructure.constant.vertex.property;

public enum ExplanationVertexProperty {
    TEXT("text");

    private String value;

    private ExplanationVertexProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
