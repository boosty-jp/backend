package co.jp.wever.graphql.application.datamodel.response.query.plan;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LearningPlanItemResponse {
    private PlanBaseResponse base;
    private PlanActionResponse action;
    private PlanStatisticsResponse statistics;
}
