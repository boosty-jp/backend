package jp.boosty.graphql.infrastructure.constant.vertex.property;


public enum PageBlockVertexProperty {
    TYPE("type"),
    DATA("data");

    private String value;

    PageBlockVertexProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
