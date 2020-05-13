package jp.boosty.backend.infrastructure.constant.vertex.property;

public enum DateProperty {
    CREATE_TIME("createTime"), UPDATE_TIME("updateTime");

    private String value;

    private DateProperty(String value) {
        this.value = value;
    }

    public static boolean isDefined(String value) {
        for (DateProperty d: DateProperty.values()) {
            if (d.value.equals(value)) {
                return true;
            }
        }
        return false;
    }

    public String getString() {
        return this.value;
    }
}
