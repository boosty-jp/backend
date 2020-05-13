package jp.boosty.backend.infrastructure.constant.vertex.property;

public enum BookTargetDescriptionProperty {
    TEXT("text"),
    NUMBER("number");

    private String value;

    private BookTargetDescriptionProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
