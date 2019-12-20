package co.jp.wever.graphql.infrastructure.constant.vertex.property;

public enum CourseVertexProperty {
    TITLE("title"),
    DESCRIPTION("description"),
    IMAGE_URL("imageUrl");

    private String value;

    private CourseVertexProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
