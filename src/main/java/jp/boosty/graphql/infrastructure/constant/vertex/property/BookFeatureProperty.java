package jp.boosty.graphql.infrastructure.constant.vertex.property;

public enum BookFeatureProperty {
    TEXT("text"),
    NUMBER("number");

    private String value;

    private BookFeatureProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
