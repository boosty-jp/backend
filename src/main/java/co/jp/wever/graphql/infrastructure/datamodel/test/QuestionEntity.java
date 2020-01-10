package co.jp.wever.graphql.infrastructure.datamodel.test;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionEntity {
    private String id;
    private String text;
    private String type;
    private int number;
    private TextAnswerEntity textAnswer;
    private List<SelectAnswerEntity> selectAnswers;
}
