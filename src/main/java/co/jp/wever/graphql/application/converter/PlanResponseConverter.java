package co.jp.wever.graphql.application.converter;

import co.jp.wever.graphql.application.datamodel.response.query.PlanResponse;
import co.jp.wever.graphql.domain.domainmodel.plan.Plan;
import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanBase;

public class PlanResponseConverter {
    public static PlanResponse toPlanResponse(Plan plan) {
        return PlanResponse.builder().baseResponse(null).elementResponses(null).build();
    }
}
