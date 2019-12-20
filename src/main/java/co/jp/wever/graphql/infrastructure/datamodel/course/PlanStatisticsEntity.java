package co.jp.wever.graphql.infrastructure.datamodel.course;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanStatisticsEntity {
    private long likeCount;
    private long learningCount;
    private long learnedCount;
}
