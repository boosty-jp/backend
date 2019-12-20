package co.jp.wever.graphql.domain.converter.plan;

import co.jp.wever.graphql.domain.domainmodel.plan.statistics.PlanLearnedCount;
import co.jp.wever.graphql.domain.domainmodel.plan.statistics.PlanLearningCount;
import co.jp.wever.graphql.domain.domainmodel.plan.statistics.PlanLikeCount;
import co.jp.wever.graphql.domain.domainmodel.plan.statistics.PlanStatistics;
import co.jp.wever.graphql.infrastructure.datamodel.course.PlanStatisticsEntity;

public class PlanStatisticsConverter {
    public static PlanStatistics toPlanStatistics(PlanStatisticsEntity planStatisticsEntity) {
        return new PlanStatistics(PlanLearnedCount.of(planStatisticsEntity.getLearnedCount()),
                                  PlanLearningCount.of(planStatisticsEntity.getLearningCount()),
                                  PlanLikeCount.of(planStatisticsEntity.getLikeCount()));
    }
}
