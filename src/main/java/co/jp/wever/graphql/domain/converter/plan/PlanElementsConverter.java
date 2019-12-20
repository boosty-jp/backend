package co.jp.wever.graphql.domain.converter.plan;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElement;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElements;

public class PlanElementsConverter {
    public static PlanElements toPlanElements(List<PlanElementInput> planElementInputs) {
        List<PlanElement> planElements =
            planElementInputs.stream().map(p -> PlanElementConverter.toPlanElement(p)).collect(Collectors.toList());

        return PlanElements.of(planElements);
    }
}
