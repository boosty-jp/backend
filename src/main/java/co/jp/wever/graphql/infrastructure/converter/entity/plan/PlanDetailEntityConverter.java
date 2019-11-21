package co.jp.wever.graphql.infrastructure.converter.entity.plan;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import co.jp.wever.graphql.infrastructure.converter.entity.tag.TagEntityConverter;
import co.jp.wever.graphql.infrastructure.converter.entity.user.UserEntityConverter;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanStatisticsEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanUserActionEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.aggregation.PlanDetailEntity;

@Component
public class PlanDetailEntityConverter {
    public static PlanDetailEntity toPlanDetailEntity(Map<String, Object> result) {
        Map<Object, Object> baseResult = (Map<Object, Object>) result.get("base");
        List<Map<Object, Object>> tagResult = (List<Map<Object, Object>>) result.get("tags");
        Map<Object, Object> authorResult = (Map<Object, Object>) result.get("author");
        List<Map<String, Object>> elementResults = (List<Map<String, Object>>) result.get("elements");

        String statusResult = result.get("status").toString();

        boolean userLiked = (boolean) result.get("userLiked");
        boolean userLearned = (boolean) result.get("userLearned");
        boolean userLearning = (boolean) result.get("userLearning");

        long like = (long) result.get("like");
        long learned = (long) result.get("learned");
        long learning = (long) result.get("learning");

        return PlanDetailEntity.builder()
                               .base(PlanBaseEntityConverter.toPlanBaseEntity(baseResult, statusResult))
                               .elementEntities(elementResults.stream()
                                                              .map(e -> PlanElementEntityConverter.toPlanElementEntity((Map<Object, Object>) e
                                                                  .get("element"), (Map<Object, Object>) e.get("edge")))
                                                              .collect(Collectors.toList()))
                               .tags(tagResult.stream()
                                              .map(t -> TagEntityConverter.toTagEntity(t))
                                              .collect(Collectors.toList()))
                               .author(UserEntityConverter.toUserEntityNoTag(authorResult))
                               .actions(PlanUserActionEntity.builder()
                                                            .liked(userLiked)
                                                            .learning(userLearning)
                                                            .learned(userLearned)
                                                            .build())
                               .statistics(PlanStatisticsEntity.builder()
                                                               .likeCount(like)
                                                               .learnedCount(learned)
                                                               .learningCount(learning)
                                                               .build())
                               .build();
    }

    public static PlanDetailEntity toPlanDetailEntityForGuest(Map<String, Object> result) {
        Map<Object, Object> baseResult = (Map<Object, Object>) result.get("base");
        List<Map<Object, Object>> tagResult = (List<Map<Object, Object>>) result.get("tags");
        Map<Object, Object> authorResult = (Map<Object, Object>) result.get("author");
        List<Map<String, Object>> elementResults = (List<Map<String, Object>>) result.get("elements");

        String statusResult = result.get("status").toString();

        boolean userLiked = false;
        boolean userLearned = false;
        boolean userLearning = false;

        long like = (long) result.get("like");
        long learned = (long) result.get("learned");
        long learning = (long) result.get("learning");

        return PlanDetailEntity.builder()
                               .base(PlanBaseEntityConverter.toPlanBaseEntity(baseResult, statusResult))
                               .elementEntities(elementResults.stream()
                                                              .map(e -> PlanElementEntityConverter.toPlanElementEntity((Map<Object, Object>) e
                                                                  .get("element"), (Map<Object, Object>) e.get("edge")))
                                                              .collect(Collectors.toList()))
                               .tags(tagResult.stream()
                                              .map(t -> TagEntityConverter.toTagEntity(t))
                                              .collect(Collectors.toList()))
                               .author(UserEntityConverter.toUserEntityNoTag(authorResult))
                               .actions(PlanUserActionEntity.builder()
                                                            .liked(userLiked)
                                                            .learning(userLearning)
                                                            .learned(userLearned)
                                                            .build())
                               .statistics(PlanStatisticsEntity.builder()
                                                               .likeCount(like)
                                                               .learnedCount(learned)
                                                               .learningCount(learning)
                                                               .build())
                               .build();
    }
}
