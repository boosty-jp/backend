package co.jp.wever.graphql.application.converter;

import co.jp.wever.graphql.application.datamodel.response.query.ElementResponse;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElement;

public class PlanElementResponseConverter {
    public static ElementResponse toPlanElementResponse(PlanElement planElement) {
        return ElementResponse.builder().id(planElement.getId()).number(planElement.getNumber()).type(planElement.getElementType()).title(planElement.getTitle()).build();
    }
}
