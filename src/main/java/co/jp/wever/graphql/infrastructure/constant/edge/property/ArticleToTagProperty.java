package co.jp.wever.graphql.infrastructure.constant.edge.property;

public enum ArticleToTagProperty {
    RELATED_TIME("relatedTime");

    private String value;

    private ArticleToTagProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
