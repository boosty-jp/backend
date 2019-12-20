package co.jp.wever.graphql.infrastructure.datamodel.course;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LearningPlanItemEntity {
    private PlanBaseEntity base;
    private PlanUserActionEntity action;
    private PlanStatisticsEntity statistics;
}
