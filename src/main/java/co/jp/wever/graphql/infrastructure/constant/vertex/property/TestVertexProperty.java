package co.jp.wever.graphql.infrastructure.constant.vertex.property;

public enum TestVertexProperty {
    TITLE("title"),
    DESCRIPTION("description");

    private String value;

    private TestVertexProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
