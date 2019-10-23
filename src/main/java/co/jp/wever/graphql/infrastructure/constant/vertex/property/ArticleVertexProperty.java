package co.jp.wever.graphql.infrastructure.constant.vertex.property;

public enum ArticleVertexProperty {
    TITLE("title"), DESCRIPTION("description"), IMAGE_URL("imageUrl"), CREATED_TIME("created_time"), UPDATED_TIME(
        "updateTime");

    private String value;

    private ArticleVertexProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
