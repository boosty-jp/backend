package co.jp.wever.graphql.infrastructure.datamodel.course;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanElementStatisticsEntity {
    private long like;
    private long learned;
}