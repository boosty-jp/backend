package co.jp.wever.graphql.infrastructure.constant.edge.property;

public enum ArticleToSectionProperty {
    NUMBER("number"), CREATE_TIME("createTime"), UPDATE_TIME("updateTime");

    private String value;

    private ArticleToSectionProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
