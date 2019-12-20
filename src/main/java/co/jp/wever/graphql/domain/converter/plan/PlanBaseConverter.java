package co.jp.wever.graphql.domain.converter.plan;

import java.util.Date;

import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanBase;
import co.jp.wever.graphql.infrastructure.datamodel.course.PlanBaseEntity;

public class PlanBaseConverter {
    public static PlanBase toPlanBase(PlanBaseEntity planBaseEntity) {
        return PlanBase.of(planBaseEntity.getId(),
                           planBaseEntity.getTitle(),
                           planBaseEntity.getDescription(),
                           planBaseEntity.getImageUrl(),
                           planBaseEntity.getStatus(),
                           new Date(planBaseEntity.getCreatedDate()),
                           new Date(planBaseEntity.getUpdatedDate()));
    }
}