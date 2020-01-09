package co.jp.wever.graphql.application.converter.test;

import org.springframework.http.HttpStatus;

import java.util.Map;

import co.jp.wever.graphql.application.datamodel.request.test.TextAnswerInput;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class TextAnswerInputConverter {
    public static TextAnswerInput toTextAnswerInput(Map<String, Object> textAnswerRequest) {
        try {
            return TextAnswerInput.builder()
                                  .answer(textAnswerRequest.get("answer").toString())
                                  .showCount((boolean) textAnswerRequest.get("showCount"))
                                  .build();
        } catch (Exception e) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }
}
