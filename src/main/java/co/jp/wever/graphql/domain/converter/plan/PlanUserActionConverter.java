package co.jp.wever.graphql.domain.converter.plan;

import co.jp.wever.graphql.domain.domainmodel.plan.action.PlanUserAction;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanUserActionEntity;

public class PlanUserActionConverter {
    public static PlanUserAction toPlanUserAction(PlanUserActionEntity planUserActionEntity){
        return new PlanUserAction(planUserActionEntity.isLiked(), planUserActionEntity.isLearning(), planUserActionEntity.isLearned());
    }
}
