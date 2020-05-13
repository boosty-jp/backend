package jp.boosty.backend.infrastructure.constant;

import org.springframework.http.HttpStatus;

import jp.boosty.backend.domain.GraphQLCustomException;

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

    public static QuestionType fromString(String value) {
        for (QuestionType t : QuestionType.values()) {
            if (t.value.equalsIgnoreCase(value)) {
                return t;
            }
        }

        throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                         GraphQLErrorMessage.INVALID_ANSWER_TYPE.getString());
    }
}
