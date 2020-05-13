package jp.boosty.backend.infrastructure.constant.edge.property;

public enum IncludeEdgeProperty {
    NUMBER("number");

    private String value;

    IncludeEdgeProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
