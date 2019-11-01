package co.jp.wever.graphql.application.converter.plan;

import co.jp.wever.graphql.application.datamodel.response.query.plan.PlanElementResponse;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElement;

public class PlanElementResponseConverter {
    public static PlanElementResponse toPlanElementResponse(PlanElement planElement) {
        return PlanElementResponse.builder()
                                  .id(planElement.getId())
                                  .number(planElement.getNumber())
                                  .type(planElement.getElementType())
                                  .imageUrl(planElement.getImageUrl())
                                  .title(planElement.getTitle())
                                  .build();
    }
}
