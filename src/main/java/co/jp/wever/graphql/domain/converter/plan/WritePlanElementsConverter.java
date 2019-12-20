package co.jp.wever.graphql.domain.converter.plan;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.domainmodel.plan.element.WritePlanElement;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PublishPlanElements;

public class WritePlanElementsConverter {
    public static PublishPlanElements toWritePlanElements(List<PlanElementInput> planElementInputs) {
        List<WritePlanElement> writePlanElements = planElementInputs.stream()
                                                                    .map(p -> WritePlanElementConverter.toWritePlanElement(
                                                                        p))
                                                                    .collect(Collectors.toList());

        return PublishPlanElements.of(writePlanElements);
    }
}
