package co.jp.wever.graphql.application.converter.plan;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.application.converter.tag.TagResponseConverter;
import co.jp.wever.graphql.application.converter.user.UserResponseConverter;
import co.jp.wever.graphql.application.datamodel.response.query.UserResponse;
import co.jp.wever.graphql.application.datamodel.response.query.plan.FamousPlanResponse;
import co.jp.wever.graphql.application.datamodel.response.query.plan.PlanBaseResponse;
import co.jp.wever.graphql.application.datamodel.response.query.plan.PlanStatisticsResponse;
import co.jp.wever.graphql.application.datamodel.response.query.tag.TagResponse;
import co.jp.wever.graphql.infrastructure.datamodel.plan.aggregation.PlanItemEntity;

public class FamousPlanResponseConverter {
    public static FamousPlanResponse toFamousPlanResponse(PlanItemEntity planItemEntity) {
        PlanBaseResponse base = PlanBaseResponseConverter.toPlanBaseResponse(planItemEntity.getBase());
        UserResponse author = UserResponseConverter.toUserResponse(planItemEntity.getAuthor());
        List<TagResponse> tags = planItemEntity.getTags()
                                               .stream()
                                               .map(t -> TagResponseConverter.toTagResponse(t))
                                               .collect(Collectors.toList());
        PlanStatisticsResponse statistics =
            PlanStatisticsResponseConverter.toPlanStatisticsResponse(planItemEntity.getStatistics());

        return FamousPlanResponse.builder()
                                 .base(base)
                                 .author(author)
                                 .tags(tags)
                                 .statistics(statistics)
                                 .elementCount(planItemEntity.getElementCount())
                                 .build();
    }
}
