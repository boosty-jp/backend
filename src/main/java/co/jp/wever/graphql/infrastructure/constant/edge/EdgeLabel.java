package co.jp.wever.graphql.infrastructure.constant.edge;

public enum EdgeLabel {
    LIKE("like"),
    LEARN("learn"),
    ONGOING("ongoing"),
    TEACH("teach"),
    INCLUDE("include"),
    PUBLISH("publish"),
    LIMITED_PUBLISH("limitedPublish"),
    SALE_PUBLISH("salePublish"),
    DRAFT("draft"),
    DELETE("delete"),
    RELATED_TO("related_to");

    private String value;

    private EdgeLabel(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
