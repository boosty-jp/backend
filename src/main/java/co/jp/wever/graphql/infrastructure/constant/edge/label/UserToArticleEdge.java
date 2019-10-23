package co.jp.wever.graphql.infrastructure.constant.edge.label;

public enum UserToArticleEdge {
    LIKED("liked"), LEARNED("learned"), DRAFTED("drafted"), PUBLISHED("published"), DELETED("deleted");

    private String value;

    private UserToArticleEdge(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
