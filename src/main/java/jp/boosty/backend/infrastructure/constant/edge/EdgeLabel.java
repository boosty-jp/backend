package jp.boosty.backend.infrastructure.constant.edge;

public enum EdgeLabel {
    VIEW("view"),
    LIKE("like"),
    LEARN("learn"),
    TEACH("teach"),
    INCLUDE("include"),
    DELETE_INCLUDE("deleteInclude"),
    PUBLISH("publish"),
    SUSPEND("suspend"),
    ANSWER("answer"),
    WRONG_ANSWER("wrongAnswer"),
    CORRECT_ANSWER("correctAnswer"),
    DRAFT("draft"),
    DELETE("delete"),
    PURCHASE("purchase"),
    RELATED_TO("related_to");

    private String value;

    private EdgeLabel(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
