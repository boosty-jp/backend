package co.jp.wever.graphql.infrastructure.constant.edge.label;

public enum UserToContentProperty {
    PUBLISHED("published"), DRAFTED("drafted"), DELETED("deleted"), LIKED("liked"), LEARNING("learning"), LEARNED("learned");

    private final String value;

    UserToContentProperty(String value) {
        this.value = value;
    }

    public static boolean isDefined(String str) {
        for (UserToContentProperty p: UserToContentProperty.values()) {
            if (p.getString().equals(str)) {
                return true;
            }
        }
        return false;
    }

    public String getString() {
        return this.value;
    }
}
