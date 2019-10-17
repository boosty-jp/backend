package co.jp.wever.graphql.domain.converter;

import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanBase;
import co.jp.wever.graphql.infrastructure.datamodel.PlanBaseEntity;

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
