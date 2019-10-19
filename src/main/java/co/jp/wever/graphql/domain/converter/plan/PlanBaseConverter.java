package co.jp.wever.graphql.domain.converter.plan;

import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanBase;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanBaseEntity;

public class PlanBaseConverter {
    public static PlanBase toPlanBase(PlanBaseEntity planBaseEntity) {
        return PlanBase.of(planBaseEntity.getTitle(),
                           planBaseEntity.getDescription(),
                           planBaseEntity.getImageUrl(),
                           planBaseEntity.getTagIds(),
                           planBaseEntity.getAuthorId(),
                           planBaseEntity.getStatus());
    }
}
