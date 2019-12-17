package co.jp.wever.graphql.application.converter.course;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.application.converter.tag.TagResponseConverter;
import co.jp.wever.graphql.application.datamodel.response.query.tag.TagResponse;
import co.jp.wever.graphql.application.datamodel.response.query.plan.PlanListItemResponse;
import co.jp.wever.graphql.domain.domainmodel.plan.PlanListItem;

public class PlanListItemResponseConverter {
    public static PlanListItemResponse toPlanListItemResponse(PlanListItem planListItem) {

        List<TagResponse> tags = planListItem.getTags()
                                             .stream()
                                             .map(t -> TagResponseConverter.toTagResponse(t))
                                             .collect(Collectors.toList());

        return PlanListItemResponse.builder()
                                   .base(PlanBaseResponseConverter.toPlanBaseResponse(planListItem.getBase()))
                                   .action(PlanActionResponseConverter.toPlanActionResponse(planListItem.getAction()))
                                   .statistics(PlanStatisticsResponseConverter.toPlanStatisticsResponse(planListItem.getStatistics()))
                                   .tags(tags)
                                   .build();
    }
}
