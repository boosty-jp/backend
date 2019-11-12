package co.jp.wever.graphql.infrastructure.constant.edge.property;

public enum ArticleToTagProperty {
    CREATED_TIME("createdTime"), UPDATED_TIME("updatedTime");

    private String value;

    private ArticleToTagProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
