package co.jp.wever.graphql.infrastructure.constant.vertex.label;


public enum VertexLabel {
    COURSE("course"),
    COURSE_SECTION("courseSection"),
    ARTICLE("article"),
    ARTICLE_TEXT("articleText"),
    ARTICLE_BLOCK("articleBlock"),
    TEST("test"),
    QUESTIONS("questions"),
    TEXT_QUESTION("textQuestion"),
    SELECT_QUESTION("selectQuestion"),
    SELECT_ANSWER("selectAnswer"),
    EXPLANATION("explanation"),
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
