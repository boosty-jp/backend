package co.jp.wever.graphql.domain.converter.plan;

import co.jp.wever.graphql.domain.domainmodel.plan.base.draft.DraftPlanBase;

public class DraftPlanBaseConverter {
    public static DraftPlanBase toDraftPlanBase(PlanBaseInput planBaseInput) {
        return DraftPlanBase.of(planBaseInput.getId(),
                            planBaseInput.getTitle(),
                            planBaseInput.getDescription(),
                            planBaseInput.getImageUrl(),
                            planBaseInput.getTags());
    }
}
