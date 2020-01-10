package co.jp.wever.graphql.application.datamodel.request.test;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionInput {
    private String questionText;
    private String type;
    private TextAnswerInput textAnswer;
    private List<SelectAnswerInput> selectAnswers;
    private List<ExplanationInput> explanations;
}
