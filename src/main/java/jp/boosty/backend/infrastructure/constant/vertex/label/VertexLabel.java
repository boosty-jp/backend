package jp.boosty.backend.infrastructure.constant.vertex.label;


public enum VertexLabel {
    BOOK("book"),
    BOOK_FEATURE("bookFeature"),
    BOOK_TARGET_DESCRIPTION("bookTargetDescription"),
    SECTION("section"),
    PAGE("page"),
    PAGE_BLOCK("articleBlock"),
    TEST("test"),
    QUESTIONS("questions"),
    TEXT_QUESTION("textQuestion"),
    SELECT_QUESTION("selectQuestion"),
    SELECT_ANSWER("selectAnswer"),
    EXPLANATION("explanation"),
    ANSWER("answer"),
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
