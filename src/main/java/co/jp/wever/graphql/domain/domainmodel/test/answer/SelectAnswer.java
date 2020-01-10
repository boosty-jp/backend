package co.jp.wever.graphql.domain.domainmodel.test.answer;

import org.springframework.http.HttpStatus;

import java.util.Objects;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class SelectAnswer {
    private Text text;

    private boolean answer;

    private SelectAnswer(Text text, boolean answer) {
        this.text = text;
        this.answer = answer;
    }

    public static SelectAnswer of(Text text, boolean answer) {
        if (Objects.isNull(text)) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(), GraphQLErrorMessage.NULL_DATA.getString());
        }

        return new SelectAnswer(text,answer);
    }

    public Text getText() {
        return text;
    }

    public boolean isAnswer() {
        return answer;
    }
}
