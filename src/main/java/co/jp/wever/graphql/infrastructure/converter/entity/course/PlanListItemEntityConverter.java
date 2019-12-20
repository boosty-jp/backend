package co.jp.wever.graphql.infrastructure.converter.entity.course;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.jp.wever.graphql.infrastructure.converter.entity.tag.TagEntityConverter;
import co.jp.wever.graphql.infrastructure.datamodel.course.PlanStatisticsEntity;
import co.jp.wever.graphql.infrastructure.datamodel.course.PlanUserActionEntity;
import co.jp.wever.graphql.infrastructure.datamodel.course.aggregation.PlanListItemEntity;

public class PlanListItemEntityConverter {
    public static PlanListItemEntity toPlanListItemEntity(Map<String, Object> result) {
        Map<Object, Object> baseResult = (Map<Object, Object>) result.get("base");
        List<Map<Object, Object>> tagResult = (List<Map<Object, Object>>) result.get("tags");

        String statusResult = result.get("status").toString();
        boolean userLiked = (boolean) result.get("userLiked");
        boolean userLearned = (boolean) result.get("userLearned");
        boolean userLearning = (boolean) result.get("userLearning");
        long like = (long) result.get("liked");
        long learned = (long) result.get("learned");
        long learning = (long) result.get("learning");

        return PlanListItemEntity.builder()
                                 .base(PlanBaseEntityConverter.toPlanBaseEntity(baseResult, statusResult))
                                 .tags(tagResult.stream()
                                                .map(t -> TagEntityConverter.toTagEntity(t))
                                                .collect(Collectors.toList()))
                                 .actions(PlanUserActionEntity.builder()
                                                              .learned(userLearned)
                                                              .liked(userLiked)
                                                              .learning(userLearning)
                                                              .build())
                                 .statistics(PlanStatisticsEntity.builder()
                                                                 .likeCount(like)
                                                                 .learnedCount(learned)
                                                                 .learningCount(learning)
                                                                 .build())
                                 .build();
    }
}
