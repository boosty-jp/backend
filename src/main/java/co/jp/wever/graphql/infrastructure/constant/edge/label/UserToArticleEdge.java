package co.jp.wever.graphql.infrastructure.constant.edge.label;

public enum UserToArticleEdge {
    LIKE("like"),
    LEARNING("learning"),
    LEARNED("learned"),
    DRAFTED("drafted"),
    PUBLISHED("published"),
    DELETED("deleted");

    private String value;

    private UserToArticleEdge(String value) {
        this.value = value;
    }
}
