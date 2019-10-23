package co.jp.wever.graphql.infrastructure.constant.edge.label;

public enum UserToSectionEdge {
    LIKED("liked"), CREATED("created"), DELETED("deleted");

    private String value;

    private UserToSectionEdge(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
