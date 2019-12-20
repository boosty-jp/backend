package co.jp.wever.graphql.infrastructure.datamodel.course;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanUserActionEntity {
    private boolean liked;
    private boolean learning;
    private boolean learned;
    private int learnedElementCount;
    private int allElementCount;
}
