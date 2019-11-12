package co.jp.wever.graphql.infrastructure.constant.vertex.property;

public enum ArticleVertexProperty {
    TITLE("title"), IMAGE_URL("imageUrl"), LIKED("liked"), LEARNED("learned"), CREATED_TIME("created_time"), UPDATED_TIME(
        "updatedTime");

    private String value;

    private ArticleVertexProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
