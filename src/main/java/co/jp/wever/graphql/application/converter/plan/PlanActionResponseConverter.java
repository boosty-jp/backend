package co.jp.wever.graphql.application.converter.plan;

import co.jp.wever.graphql.application.datamodel.response.query.plan.PlanActionResponse;
import co.jp.wever.graphql.domain.domainmodel.plan.action.PlanUserAction;

public class PlanActionResponseConverter {
    public static PlanActionResponse toPlanActionResponse(PlanUserAction planUserAction) {
        return PlanActionResponse.builder()
                                 .learned(planUserAction.isLearned())
                                 .liked(planUserAction.isLiked())
                                 .learning(planUserAction.isLearning())
                                 .build();
    }
}
