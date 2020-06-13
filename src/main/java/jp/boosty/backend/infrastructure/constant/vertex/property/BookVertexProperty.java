package jp.boosty.backend.infrastructure.constant.vertex.property;

public enum BookVertexProperty {
    TITLE("title"),
    DESCRIPTION("description"),
    PRICE("price"),
    LEVEL_START("levelStart"),
    LEVEL_END("levelEnd"),
    IMAGE_URL("imageUrl"),
    MEANINGFUL("meaningful");

    private String value;

    private BookVertexProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
