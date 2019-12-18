package co.jp.wever.graphql.infrastructure.constant.edge.label;

public enum ArticleToSkillEdge {
    TEACH("teach");

    private String value;

    private ArticleToSkillEdge(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
