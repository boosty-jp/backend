package co.jp.wever.graphql.application.converter.test;

import org.springframework.http.HttpStatus;

import java.util.Map;

import co.jp.wever.graphql.application.datamodel.request.test.SelectAnswerInput;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class SelectAnswerInputConverter {
    public static SelectAnswerInput toSelectAnswerInput(Map<String, Object> selectAnswerRequest) {
        try {
            return SelectAnswerInput.builder()
                            .text(selectAnswerRequest.get("text").toString())
                            .answer((boolean) selectAnswerRequest.get("answer"))
                            .build();
        } catch (Exception e) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }
}
