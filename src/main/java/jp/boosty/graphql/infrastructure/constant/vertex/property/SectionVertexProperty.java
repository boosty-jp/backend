package jp.boosty.graphql.infrastructure.constant.vertex.property;

public enum SectionVertexProperty {
    TITLE("title");

    private String value;

    private SectionVertexProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
