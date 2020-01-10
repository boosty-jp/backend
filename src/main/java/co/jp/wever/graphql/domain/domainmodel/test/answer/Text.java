package co.jp.wever.graphql.domain.domainmodel.test.answer;

import org.springframework.http.HttpStatus;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import io.netty.util.internal.StringUtil;

public class Text {
    private String value;
    private final static int MAX_WORD_COUNT = 100;

    private Text(String value) {
        this.value = value;
    }

    public static Text of(String value) {
        if (StringUtil.isNullOrEmpty(value)) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.TEXT_ANSWER_EMPTY.getString());
        }

        if (value.length() > MAX_WORD_COUNT) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.TEXT_ANSWER_OVER.getString());
        }

        return new Text(value);
    }

    public String getValue() {
        return value;
    }
}
