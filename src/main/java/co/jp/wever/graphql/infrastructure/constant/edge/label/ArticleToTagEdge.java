package co.jp.wever.graphql.infrastructure.constant.edge.label;

public enum ArticleToTagEdge {
    RELATED("related");

    private String value;

    private ArticleToTagEdge(String value) {
        this.value = value;
    }
}
