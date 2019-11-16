package co.jp.wever.graphql.infrastructure.constant.edge.property;

public enum PlanToTagProperty {
    CREATED_TIME("createdTime"), UPDATED_TIME("updatedTime");

    private String value;

    private PlanToTagProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
