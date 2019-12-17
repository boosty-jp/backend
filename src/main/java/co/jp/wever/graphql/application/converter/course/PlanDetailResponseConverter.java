package co.jp.wever.graphql.application.converter.course;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.application.converter.tag.TagResponseConverter;
import co.jp.wever.graphql.application.converter.user.UserResponseConverter;
import co.jp.wever.graphql.application.datamodel.response.query.tag.TagResponse;
import co.jp.wever.graphql.application.datamodel.response.query.user.UserResponse;
import co.jp.wever.graphql.application.datamodel.response.query.plan.PlanActionResponse;
import co.jp.wever.graphql.application.datamodel.response.query.plan.PlanBaseResponse;
import co.jp.wever.graphql.application.datamodel.response.query.plan.PlanDetailResponse;
import co.jp.wever.graphql.application.datamodel.response.query.plan.PlanElementResponse;
import co.jp.wever.graphql.application.datamodel.response.query.plan.PlanStatisticsResponse;
import co.jp.wever.graphql.domain.domainmodel.plan.PlanDetail;

public class PlanDetailResponseConverter {
    public static PlanDetailResponse toPlanResponse(PlanDetail planDetail) {
        PlanBaseResponse base = PlanBaseResponseConverter.toPlanBaseResponse(planDetail.getBase());
        UserResponse author = UserResponseConverter.toUserResponse(planDetail.getAuthor());
        List<TagResponse> tags =
            planDetail.getTags().stream().map(t -> TagResponseConverter.toTagResponse(t)).collect(Collectors.toList());
        List<PlanElementResponse> elements = planDetail.getElements()
                                                       .stream()
                                                       .map(e -> PlanElementResponseConverter.toPlanElementResponse(e))
                                                       .sorted(Comparator.comparing(PlanElementResponse::getNumber)) //番号順にする
                                                       .collect(Collectors.toList());
        PlanStatisticsResponse statistics =
            PlanStatisticsResponseConverter.toPlanStatisticsResponse(planDetail.getStatistics());
        PlanActionResponse actions = PlanActionResponseConverter.toPlanActionResponse(planDetail.getAction());

        return PlanDetailResponse.builder()
                                 .base(base)
                                 .author(author)
                                 .tags(tags)
                                 .elements(elements)
                                 .statistics(statistics)
                                 .action(actions)
                                 .build();
    }
}
