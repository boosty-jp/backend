package co.jp.wever.graphql.infrastructure.constant.vertex.property;

public enum TagVertexProperty {
    NAME("name"), CREATED_TIME("createdTime");

    private String value;

    private TagVertexProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
