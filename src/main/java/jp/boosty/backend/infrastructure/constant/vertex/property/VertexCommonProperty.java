package jp.boosty.backend.infrastructure.constant.vertex.property;

public enum VertexCommonProperty {
    Type("type");

    private String value;

    private VertexCommonProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
