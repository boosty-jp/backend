package co.jp.wever.graphql.infrastructure.constant.edge.label;

public enum UserToUserEdge {
    FOLLOW("follow");


    private String value;

    private UserToUserEdge(String value) {
        this.value = value;
    }
}
