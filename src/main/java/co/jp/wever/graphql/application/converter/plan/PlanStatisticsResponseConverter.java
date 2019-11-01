package co.jp.wever.graphql.application.converter.plan;

import co.jp.wever.graphql.application.datamodel.response.query.plan.PlanStatisticsResponse;
import co.jp.wever.graphql.domain.domainmodel.plan.statistics.PlanStatistics;

public class PlanStatisticsResponseConverter {
    public static PlanStatisticsResponse toPlanStatisticsResponse(PlanStatistics planStatistics) {
        return PlanStatisticsResponse.builder()
                                     .learned(planStatistics.getLearnedCount().getValue())
                                     .learning(planStatistics.getLearningCount().getValue())
                                     .like(planStatistics.getLikeCount().getValue())
                                     .build();
    }
}
