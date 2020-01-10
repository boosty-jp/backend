package co.jp.wever.graphql.infrastructure.datamodel.test;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TextAnswerEntity {
    private String answer;
    private boolean showCount;
}
