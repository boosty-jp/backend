package co.jp.wever.graphql.infrastructure.constant.vertex.property;

public enum ArticleVertexProperty {
    TITLE("title"),
    IMAGE_URL("imageUrl");

    private String value;

    private ArticleVertexProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
