package co.jp.wever.graphql.application.converter;

import co.jp.wever.graphql.application.datamodel.response.query.PlanBaseResponse;
import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanBase;

public class PlanBaseResponseConverter {
    public static PlanBaseResponse toPlanBaseResponse(PlanBase planBase) {
        return PlanBaseResponse.builder()
                               .id(planBase.getPlanId())
                               .title(planBase.getTitle())
                               .description(planBase.getDescription())
                               .tags(null)
                               .image(planBase.getImageUrl())
                               .status(planBase.getStatus().name())
                               .author(null)
                               .build();
    }
}
