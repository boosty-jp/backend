package co.jp.wever.graphql.infrastructure.constant.vertex.property;

public enum ArticleVertexProperty {
    TITLE("title"),
    DESCRIPTION("description"),
    IMAGE_URL("imageUrl"),
    UPDATE_TIME("updateTime");

    private String value;

    private ArticleVertexProperty(String value) {
        this.value = value;
    }
}
