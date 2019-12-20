package co.jp.wever.graphql.infrastructure.constant.vertex.label;


public enum VertexLabel {
    COURSE("course"),
    SECTION("section"),
    ARTICLE("article"),
    ARTICLE_TEXT("articleText"),
    ARTICLE_BLOCK("articleBlock"),
    USER("user"),
    TAG("tag"),
    SKILL("skill");

    private String value;

    private VertexLabel(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
