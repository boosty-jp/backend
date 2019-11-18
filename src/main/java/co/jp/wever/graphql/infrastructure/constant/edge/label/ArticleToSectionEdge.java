package co.jp.wever.graphql.infrastructure.constant.edge.label;

public enum ArticleToSectionEdge {
    INCLUDE("include");

    private String value;

    private ArticleToSectionEdge(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}