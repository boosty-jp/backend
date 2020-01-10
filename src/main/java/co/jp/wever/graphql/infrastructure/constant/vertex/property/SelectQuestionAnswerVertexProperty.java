package co.jp.wever.graphql.infrastructure.constant.vertex.property;

public enum SelectQuestionAnswerVertexProperty {
    IS_ANSWER("isAnswer"), TEXT("text");

    private String value;

    private SelectQuestionAnswerVertexProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
