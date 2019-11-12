package co.jp.wever.graphql.infrastructure.converter.entity.plan;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.jp.wever.graphql.infrastructure.converter.entity.tag.TagEntityConverter;
import co.jp.wever.graphql.infrastructure.converter.entity.user.UserEntityConverter;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanStatisticsEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.aggregation.FamousPlanEntity;

public class FamousPlanEntityConverter {
    public static FamousPlanEntity toFamousPlanEntity(Map<String, Object> result) {
        Map<Object, Object> baseResult = (Map<Object, Object>) result.get("base");
        List<Map<Object, Object>> tagResult = (List<Map<Object, Object>>) result.get("tags");
        Map<Object, Object> authorResult = (Map<Object, Object>) result.get("author");

        String statusResult = result.get("status").toString();
        long like = (long) result.get("like");
        long learned = (long) result.get("learned");
        long learning = (long) result.get("learning");

        return FamousPlanEntity.builder()
                               .base(PlanBaseEntityConverter.toPlanBaseEntity(baseResult, statusResult))
                               .tags(tagResult.stream()
                                              .map(t -> TagEntityConverter.toTagEntity(t))
                                              .collect(Collectors.toList()))
                               .author(UserEntityConverter.toUserEntityNoTag(authorResult))
                               .statistics(PlanStatisticsEntity.builder()
                                                               .likeCount(like)
                                                               .learnedCount(learned)
                                                               .learningCount(learning)
                                                               .build())
                               .build();
    }
}
