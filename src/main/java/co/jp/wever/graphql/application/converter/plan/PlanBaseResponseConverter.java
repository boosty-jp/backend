package co.jp.wever.graphql.application.converter.plan;

import co.jp.wever.graphql.application.datamodel.response.query.plan.PlanBaseResponse;
import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanBase;

public class PlanBaseResponseConverter {
    public static PlanBaseResponse toPlanBaseResponse(PlanBase planBase) {
        return PlanBaseResponse.builder()
                               .id(planBase.getPlanId())
                               .title(planBase.getTitle())
                               .description(planBase.getDescription())
                               .tags(null)
                               .image(planBase.getImageUrl())
                               .status(planBase.getStatus().getString())
                               .author(null)
                               .build();
    }
}
