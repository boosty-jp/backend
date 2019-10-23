package co.jp.wever.graphql.infrastructure.constant.edge.property;

public enum UserToUserProperty {
    FOLLOWED_TIME("followedTime");

    private String value;

    private UserToUserProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
