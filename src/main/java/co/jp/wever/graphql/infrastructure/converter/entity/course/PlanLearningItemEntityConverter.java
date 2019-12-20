package co.jp.wever.graphql.infrastructure.converter.entity.course;

import java.util.List;
import java.util.Map;

import co.jp.wever.graphql.infrastructure.datamodel.course.LearningPlanItemEntity;
import co.jp.wever.graphql.infrastructure.datamodel.course.PlanStatisticsEntity;
import co.jp.wever.graphql.infrastructure.datamodel.course.PlanUserActionEntity;

public class PlanLearningItemEntityConverter {
    public static LearningPlanItemEntity toPlanLearningItemEntity(Map<String, Object> result) {
        Map<Object, Object> baseResult = (Map<Object, Object>) result.get("base");
        List<String> allElementsResult = (List<String>) result.get("allElements");
        List<String> learnedElementsResult = (List<String>) result.get("learnedElements");
        long like = (long) result.get("liked");
        long learned = (long) result.get("learned");
        long learning = (long) result.get("learning");

        return LearningPlanItemEntity.builder()
                                     .base(PlanBaseEntityConverter.toPlanBaseEntity(baseResult,
                                                                                    UserToPlanEdge.PUBLISHED.getString())) //TODO: status取得しないので決め打ち
                                     .action(PlanUserActionEntity.builder()
                                                                 .allElementCount(allElementsResult.size())
                                                                 .learnedElementCount(learnedElementsResult.size())
                                                                 .build())
                                     .statistics(PlanStatisticsEntity.builder()
                                                                     .learningCount(learning)
                                                                     .learnedCount(learned)
                                                                     .likeCount(like)
                                                                     .build())
                                     .build();
    }
}
