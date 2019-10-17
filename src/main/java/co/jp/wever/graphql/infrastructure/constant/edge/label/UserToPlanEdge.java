package co.jp.wever.graphql.infrastructure.constant.edge.label;

public enum UserToPlanEdge {
    PUBLISH("publish"), DRAFT("draft"), DELETE("delete"), LIKE("like"), LEARNING("learning"), LEARNED("learned");

    private String value;

    private UserToPlanEdge(String value) {
        this.value = value;
    }
}
