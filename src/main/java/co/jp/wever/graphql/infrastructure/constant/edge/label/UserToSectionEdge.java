package co.jp.wever.graphql.infrastructure.constant.edge.label;

public enum UserToSectionEdge {
    LIKE("like"), CREATED("created"), DELETED("deleted");

    private String value;

    private UserToSectionEdge(String value) {
        this.value = value;
    }
}
