package jp.boosty.backend.infrastructure.constant.vertex.property;

public enum TagVertexProperty {
    NAME("name");

    private String value;

    private TagVertexProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
