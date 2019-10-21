package co.jp.wever.graphql.infrastructure.constant.vertex.property;

public enum UserVertexProperty {
    DISPLAY_NAME("displayName"), IMAGE_URL("imageUrl"), DESCRIPTION("description"), URL("url"), CREATE_DATE("createDate"), UPDATE_DATE(
        "updateDate");

    private String value;

    private UserVertexProperty(String value) {
        this.value = value;
    }
}
