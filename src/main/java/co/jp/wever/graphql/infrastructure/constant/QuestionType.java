package co.jp.wever.graphql.infrastructure.constant;

public enum QuestionType {
    TEXT("text"),
    SELECT("select");

    private String value;

    QuestionType(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }

}
