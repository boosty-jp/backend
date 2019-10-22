package co.jp.wever.graphql.infrastructure.constant.edge.property;

public enum ArticleToSectionProperty {
    NUMBER("number"), CREATED_TIME("createdTime"), UPDATED_TIME("updatedTime");

    private String value;

    private ArticleToSectionProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
