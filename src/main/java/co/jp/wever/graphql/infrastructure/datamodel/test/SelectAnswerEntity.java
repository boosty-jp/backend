package co.jp.wever.graphql.infrastructure.datamodel.test;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SelectAnswerEntity {
    private boolean isAnswer;
    private String text;
    private int number;
}
