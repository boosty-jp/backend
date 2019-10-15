package co.jp.wever.graphql.infrastructure.constant;

public enum UserPlanEdge {
    PUBLISH("publish"), DRAFT("draft"), DELETE("delete"), LIKE("like"), LEARNING("learning"), LEARNED("learned");

    private String value;

    private UserPlanEdge(String value) {
        this.value = value;
    }
}
