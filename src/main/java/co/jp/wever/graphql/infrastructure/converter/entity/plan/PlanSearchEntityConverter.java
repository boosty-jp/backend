package co.jp.wever.graphql.infrastructure.converter.entity.plan;

import co.jp.wever.graphql.domain.domainmodel.plan.PublishPlan;
import co.jp.wever.graphql.infrastructure.datamodel.algolia.PlanSearchEntity;

public class PlanSearchEntityConverter {
    public static PlanSearchEntity toPlanSearchEntity(PublishPlan publishPlan, String authorId, long updateDate) {
        return PlanSearchEntity.builder()
                               .objectID(publishPlan.getId())
                               .imageUrl(publishPlan.getImageUrl())
                               .title(publishPlan.getTitle())
                               .updateDate(updateDate)
                               .authorId(authorId)
                               .tags(publishPlan.getTagIds())
                               .build();
    }
}
