package co.jp.wever.graphql.infrastructure.constant.edge.label;

public enum UserToPlanEdge {
    PUBLISHED("published"), DRAFTED("drafted"), DELETED("deleted"), LIKED("liked"), LEARNING("learning"), LEARNED("learned");

    private String value;

    private UserToPlanEdge(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
