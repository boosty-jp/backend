package co.jp.wever.graphql.infrastructure.constant.edge.property;

public enum UserToTagProperty {
    RELATED_TIME("relatedTime");

    private String value;

    private UserToTagProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
