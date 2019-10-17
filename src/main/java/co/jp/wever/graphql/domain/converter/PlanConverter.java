package co.jp.wever.graphql.domain.converter;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.domainmodel.plan.Plan;
import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanBase;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElement;
import co.jp.wever.graphql.infrastructure.datamodel.PlanEntity;

public class PlanConverter {
    public static Plan toPlan(PlanEntity planEntity) {
        PlanBase planBase = PlanBaseConverter.toPlanBase(planEntity.getBaseEntity());
        List<PlanElement> elements = planEntity.getElementEntities()
                                               .stream()
                                               .map(p -> PlanElementConverter.toPlanElement(p))
                                               .collect(Collectors.toList());

        return new Plan(planBase, elements);
    }
}
