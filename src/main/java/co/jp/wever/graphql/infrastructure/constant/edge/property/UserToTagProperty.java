package co.jp.wever.graphql.infrastructure.constant.edge.property;

public enum UserToTagProperty {
    CREATED_TIME("createdTime"), UPDATED_TIME("updatedTime");

    private String value;

    private UserToTagProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
