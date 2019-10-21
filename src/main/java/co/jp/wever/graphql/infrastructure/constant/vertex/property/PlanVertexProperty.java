package co.jp.wever.graphql.infrastructure.constant.vertex.property;

public enum PlanVertexProperty {
    TITLE("title"), DESCRIPTION("description"), IMAGE_URL("imageUrl");

    private String value;

    private PlanVertexProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
