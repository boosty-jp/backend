package co.jp.wever.graphql.application.datamodel.request.test;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SelectAnswerInput {
    private String text;
    private boolean answer;
}
