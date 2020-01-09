package co.jp.wever.graphql.application.converter.test;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

import co.jp.wever.graphql.application.datamodel.request.test.ExplanationInput;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class ExplanationInputConverter {
    public static ExplanationInput toExplanationInput(Map<String, Object> explanationRequest) {
        try {
            return ExplanationInput.builder()
                                   .text(explanationRequest.get("text").toString())
                                   .referenceIds((List<String>) explanationRequest.get("referenceIds"))
                                   .build();
        } catch (Exception e) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }
}
