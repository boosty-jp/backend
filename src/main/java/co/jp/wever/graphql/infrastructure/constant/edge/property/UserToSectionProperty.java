package co.jp.wever.graphql.infrastructure.constant.edge.property;

public enum UserToSectionProperty {
    LIKED_DATE("likedDate"), CREATED_DATE("createdDate"), UPDATED_DATE("updatedDate"), DELETED_DATE("deletedDate");


    private String value;

    private UserToSectionProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
