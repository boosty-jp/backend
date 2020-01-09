package co.jp.wever.graphql.application.datamodel.request.test;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TextAnswerInput {
    private String answer;
    private boolean showCount;
}
