package co.jp.wever.graphql.infrastructure.constant.edge.property;

public enum PlanToPlanElementProperty {
    NUMBER("number"), INCLUDE_TIME("includeTime");

    private String value;

    private PlanToPlanElementProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
