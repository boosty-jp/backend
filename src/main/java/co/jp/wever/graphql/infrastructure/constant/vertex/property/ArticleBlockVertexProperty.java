package co.jp.wever.graphql.infrastructure.constant.vertex.property;


public enum ArticleBlockVertexProperty {
    TYPE("type"),
    DATA("data");

    private String value;

    ArticleBlockVertexProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
