package co.jp.wever.graphql.application.converter.course;

import co.jp.wever.graphql.application.datamodel.response.query.plan.PlanElementActionResponse;
import co.jp.wever.graphql.application.datamodel.response.query.plan.PlanElementDetailResponse;
import co.jp.wever.graphql.application.datamodel.response.query.plan.PlanElementStatisticsResponse;
import co.jp.wever.graphql.domain.domainmodel.plan.element.FindPlanElementDetail;

public class PlanElementDetailResponseConverter {
    public static PlanElementDetailResponse toPlanElementDetailResponse(FindPlanElementDetail findPlanElementDetail) {
        return PlanElementDetailResponse.builder()
                                        .id(findPlanElementDetail.getId())
                                        .imageUrl(findPlanElementDetail.getImageUrl())
                                        .number(findPlanElementDetail.getNumber())
                                        .title(findPlanElementDetail.getTitle())
                                        .statistics(PlanElementStatisticsResponse.builder()
                                                                                 .like(findPlanElementDetail.getStatistics()
                                                                                                            .getLike())
                                                                                 .learned(findPlanElementDetail.getStatistics()
                                                                                                               .getLearned())
                                                                                 .build())
                                        .action(PlanElementActionResponse.builder()
                                                                         .liked(findPlanElementDetail.getAction()
                                                                                                     .isLiked())
                                                                         .learned(findPlanElementDetail.getAction()
                                                                                                       .isLearned())
                                                                         .build())
                                        .build();
    }
}