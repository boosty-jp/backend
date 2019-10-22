package co.jp.wever.graphql.infrastructure.constant.edge.property;

public enum UserToSectionProperty {
    LIKED_TIME("likedTime"), CREATED_TIME("createdTime"), UPDATED_TIME("updatedTime"), DELETED_TIME("deletedTime");


    private String value;

    private UserToSectionProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
