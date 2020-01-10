package co.jp.wever.graphql.infrastructure.constant.vertex.property;

public enum TextQuestionVertexProperty {
    QUESTION_TEXT("questionText"),
    ANSWER("answer"),
    SHOW_COUNT("showCount");

    private String value;

    private TextQuestionVertexProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
