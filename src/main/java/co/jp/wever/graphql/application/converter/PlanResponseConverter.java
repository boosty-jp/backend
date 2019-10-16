package co.jp.wever.graphql.application.converter;

import java.util.stream.Collectors;

import co.jp.wever.graphql.application.datamodel.response.query.PlanResponse;
import co.jp.wever.graphql.domain.domainmodel.plan.Plan;

public class PlanResponseConverter {
    public static PlanResponse toPlanResponse(Plan plan) {
        return PlanResponse.builder().id(plan.getPlanId()).description(plan.getDescription()).image(plan.getImageUrl()).elementResponses(plan.getElements()
                                                                                                                                             .stream()
                                                                                                                                             .map(p -> PlanElementResponseConverter.toPlanElementResponse(
                                                                                                                                                 p))
                                                                                                                                             .collect(Collectors.toList())).build();
    }
}
