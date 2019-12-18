package co.jp.wever.graphql.infrastructure.constant.edge.property;

public enum DateProperty {
    CREATED_TIME("createdTime"), UPDATED_TIME("updatedTime");

    private final String value;

    DateProperty(String value) {
        this.value = value;
    }

    public static boolean isDefined(String str) {
        for (DateProperty date : DateProperty.values()) {
            if (date.getString().equals(str)) {
                return true;
            }
        }
        return false;
    }

    public String getString() {
        return this.value;
    }
}
