package co.jp.wever.graphql.domain.converter.plan;

import java.util.List;

import co.jp.wever.graphql.application.datamodel.request.PlanBaseInput;
import co.jp.wever.graphql.application.datamodel.request.PlanElementInput;
import co.jp.wever.graphql.domain.domainmodel.plan.PublishPlan;
import co.jp.wever.graphql.domain.domainmodel.plan.base.publish.PublishPlanBase;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PublishPlanElements;

public class PublishPlanConverter {
    public static PublishPlan toPublishPlan(PlanBaseInput baseInput, List<PlanElementInput> elementInputs) {
        PublishPlanBase publishPlanBase = PublishPlanBaseConverter.toPublishPlanBase(baseInput);
        PublishPlanElements publishPlanElements = WritePlanElementsConverter.toWritePlanElements(elementInputs);

        return PublishPlan.of(publishPlanBase, publishPlanElements);
    }
}
