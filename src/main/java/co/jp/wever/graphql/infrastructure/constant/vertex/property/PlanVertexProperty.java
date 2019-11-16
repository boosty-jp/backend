package co.jp.wever.graphql.infrastructure.constant.vertex.property;

public enum PlanVertexProperty {
    TITLE("title"), DESCRIPTION("description"), LIKED("liked"), LEARNED("learned"), LEARNING("learning"), IMAGE_URL(
        "imageUrl"), CREATED_TIME("created_time"), UPDATED_TIME("updatedTime");

    private String value;

    private PlanVertexProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
