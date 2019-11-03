package co.jp.wever.graphql.domain.converter.plan;

import java.util.List;

import co.jp.wever.graphql.application.datamodel.request.PlanBaseInput;
import co.jp.wever.graphql.application.datamodel.request.PlanElementInput;
import co.jp.wever.graphql.domain.domainmodel.plan.DraftPlan;
import co.jp.wever.graphql.domain.domainmodel.plan.base.draft.DraftPlanBase;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElements;

public class DraftPlanConverter {
    public static DraftPlan toDraftPlan(PlanBaseInput baseInput, List<PlanElementInput> elementInputs) {
        DraftPlanBase base = DraftPlanBaseConverter.toDraftPlanBase(baseInput);
        PlanElements elements = PlanElementsConverter.toPlanElements(elementInputs);

        return new DraftPlan(base, elements);

    }
}
