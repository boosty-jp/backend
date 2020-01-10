package co.jp.wever.graphql.infrastructure.constant.vertex.property;

public enum SelectQuestionVertexProperty {
    QUESTION_TEXT("questionText");

    private String value;

    private SelectQuestionVertexProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
