package co.jp.wever.graphql.application.converter.plan;

import co.jp.wever.graphql.application.datamodel.response.query.plan.PlanResponse;
import co.jp.wever.graphql.domain.domainmodel.plan.Plan;

public class PlanResponseConverter {
    public static PlanResponse toPlanResponse(Plan plan) {
        return PlanResponse.builder().baseResponse(null).elementResponses(null).build();
    }
}
