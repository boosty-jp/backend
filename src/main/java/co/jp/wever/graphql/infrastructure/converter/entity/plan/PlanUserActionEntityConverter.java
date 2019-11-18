package co.jp.wever.graphql.infrastructure.converter.entity.plan;

import java.util.List;

import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToPlanEdge;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanUserActionEntity;

public class PlanUserActionEntityConverter {
    public static PlanUserActionEntity toPlanUserActionEntity(List<String> actions) {
        boolean liked = false;
        boolean learning = false;
        boolean learned = false;
        for (String action : actions) {
            if (action.equals(UserToPlanEdge.LEARNING.getString())) {
                learning = true;
            }
            if (action.equals(UserToPlanEdge.LEARNED.getString())) {
                learned = true;
            }

            if (action.equals(UserToPlanEdge.LIKED.getString())) {
                liked = true;
            }
        }
        return PlanUserActionEntity.builder().learned(learned).liked(liked).learning(learning).build();
    }
}