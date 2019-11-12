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
import co.jp.wever.graphql.infrastructure.datamodel.plan.aggregation.FamousPlanEntity;

public class FamousPlanResponseConverter {
    public static FamousPlanResponse toFamousPlanResponse(FamousPlanEntity famousPlanEntity) {
        PlanBaseResponse base = PlanBaseResponseConverter.toPlanBaseResponse(famousPlanEntity.getBase());
        UserResponse author = UserResponseConverter.toUserResponse(famousPlanEntity.getAuthor());
        List<TagResponse> tags = famousPlanEntity.getTags()
                                                 .stream()
                                                 .map(t -> TagResponseConverter.toTagResponse(t))
                                                 .collect(Collectors.toList());
        PlanStatisticsResponse statistics =
            PlanStatisticsResponseConverter.toPlanStatisticsResponse(famousPlanEntity.getStatistics());

        return FamousPlanResponse.builder().base(base).author(author).tags(tags).statistics(statistics).build();
    }
}
