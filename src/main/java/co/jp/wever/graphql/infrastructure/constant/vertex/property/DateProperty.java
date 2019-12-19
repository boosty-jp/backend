package co.jp.wever.graphql.infrastructure.constant.vertex.property;

public enum DateProperty {
    CREATE_TIME("createTime"), UPDATE_TIME("updateTime");

    private String value;

    private DateProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
