package co.jp.wever.graphql.application.converter.test;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.jp.wever.graphql.application.datamodel.request.test.QuestionInput;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class QuestionInputConverter {
    private static final String SELECT_ANSWER = "select";
    private static final String TEXT_ANSWER = "text";

    public static QuestionInput toQuestionInput(Map<String, Object> questionRequest) {
        try {
            String type = questionRequest.get("type").toString();
            List<Map<String, Object>> explanations = (List<Map<String, Object>>) questionRequest.get("explanations");

            if (type.equals(SELECT_ANSWER)) {
                List<Map<String, Object>> selectAnswers =
                    (List<Map<String, Object>>) questionRequest.get("selectAnswers");

                return QuestionInput.builder()
                                    .questionText(questionRequest.get("questionText").toString())
                                    .type(type)
                                    .selectAnswers(selectAnswers.stream()
                                                                .map(s -> SelectAnswerInputConverter.toSelectAnswerInput(
                                                                    s))
                                                                .collect(Collectors.toList()))
                                    .explanations(explanations.stream()
                                                              .map(e -> ExplanationInputConverter.toExplanationInput(e))
                                                              .collect(Collectors.toList()))
                                    .build();
            } else if (type.equals(TEXT_ANSWER)) {
                Map<String, Object> textAnswers = (Map<String, Object>) questionRequest.get("textAnswers");

                return QuestionInput.builder()
                                    .questionText(questionRequest.get("questionText").toString())
                                    .type(type)
                                    .textAnswers(TextAnswerInputConverter.toTextAnswerInput(textAnswers))
                                    .explanations(explanations.stream()
                                                              .map(e -> ExplanationInputConverter.toExplanationInput(e))
                                                              .collect(Collectors.toList()))
                                    .build();
            }
        } catch (Exception e) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }

        // 定義している解答タイプ以外のリクエストはエラーにする
        throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                         GraphQLErrorMessage.INVALID_QUESTION_ANSWER_TYPE.getString());
    }
}
