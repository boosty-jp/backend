package co.jp.wever.graphql.infrastructure.converter.entity.plan;

import co.jp.wever.graphql.domain.domainmodel.plan.PublishPlan;
import co.jp.wever.graphql.infrastructure.datamodel.algolia.PlanSearchEntity;

public class PlanSearchEntityConverter {
    public static PlanSearchEntity toPlanSearchEntity(PublishPlan publishPlan, String authorId, long updateDate) {
        return PlanSearchEntity.builder()
                               .objectID(publishPlan.getId())
                               .imageUrl(publishPlan.getImageUrl())
                               .title(publishPlan.getTitle())
                               .description(publishPlan.getDescription())
                               .updateDate(updateDate)
                               .authorId(authorId)
                               .tags(publishPlan.getTagIds())
                               .like(0)
                               .learned(0)
                               .build();
    }
}