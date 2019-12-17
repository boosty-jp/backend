package co.jp.wever.graphql.application.converter.course;

import co.jp.wever.graphql.application.datamodel.response.query.plan.PlanElementResponse;
import co.jp.wever.graphql.domain.domainmodel.plan.element.FindPlanElement;

public class PlanElementResponseConverter {
    public static PlanElementResponse toPlanElementResponse(FindPlanElement element) {
        return PlanElementResponse.builder()
                                  .id(element.getId())
                                  .number(element.getNumber())
                                  .type(element.getType())
                                  .imageUrl(element.getImageUrl())
                                  .title(element.getTitle())
                                  .build();
    }
}
