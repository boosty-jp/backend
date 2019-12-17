package co.jp.wever.graphql.application.converter.course;

import co.jp.wever.graphql.application.datamodel.response.query.plan.PlanStatisticsResponse;
import co.jp.wever.graphql.domain.domainmodel.plan.statistics.PlanStatistics;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanStatisticsEntity;

public class PlanStatisticsResponseConverter {
    public static PlanStatisticsResponse toPlanStatisticsResponse(PlanStatistics planStatistics) {
        return PlanStatisticsResponse.builder()
                                     .learned(planStatistics.getLearnedCount().getValue())
                                     .learning(planStatistics.getLearningCount().getValue())
                                     .like(planStatistics.getLikeCount().getValue())
                                     .build();
    }

    public static PlanStatisticsResponse toPlanStatisticsResponse(PlanStatisticsEntity planStatistics) {
        return PlanStatisticsResponse.builder()
                                     .learned(planStatistics.getLearnedCount())
                                     .learning(planStatistics.getLearningCount())
                                     .like(planStatistics.getLikeCount())
                                     .build();
    }
}
