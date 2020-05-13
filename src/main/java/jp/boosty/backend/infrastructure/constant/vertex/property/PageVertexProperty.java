package jp.boosty.backend.infrastructure.constant.vertex.property;

public enum PageVertexProperty {
    TITLE("title"),
    TEXT("text"),
    CAN_PREVIEW("canPreview");

    private String value;

    private PageVertexProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
