package co.jp.wever.graphql.infrastructure.constant.edge.property;

public enum PlanToTagProperty {
    RELATED_TIME("relatedTime");

    private String value;

    private PlanToTagProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
