package co.jp.wever.graphql.infrastructure.constant.edge.label;

public enum UserToUserEdge {
    FOLLOWED("followed");


    private String value;

    private UserToUserEdge(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
