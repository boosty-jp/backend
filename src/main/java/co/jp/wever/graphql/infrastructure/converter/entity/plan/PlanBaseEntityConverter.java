package co.jp.wever.graphql.infrastructure.converter.entity.plan;

import java.util.Map;

import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanBase;
import co.jp.wever.graphql.infrastructure.converter.common.VertexConverter;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanBaseEntity;

public class PlanBaseEntityConverter {
    public static PlanBaseEntity toPlanBaseEntity(PlanBase planBase) {
        return PlanBaseEntity.builder()
                             .title(planBase.getTitle())
                             .description(planBase.getDescription())
                             .imageUrl(planBase.getImageUrl())
                             .tagIds(planBase.getTagIds())
                             .status(planBase.getStatus().getString())
                             .authorId(planBase.getAuthorId().getValue())
                             .build();
    }

    public static PlanBaseEntity toPlanBaseEntity(
        Map<Object, Object> planVertex,
        Map<Object, Object> tagVertex,
        Map<Object, Object> userVertex,
        Map<Object, Object> statusVertex) {

        return PlanBaseEntity.builder()
                             .title(VertexConverter.toString("title", planVertex))
                             .imageUrl(VertexConverter.toString("image", planVertex))
                             .description(VertexConverter.toString("description", planVertex))
                             .tagIds(null)   // TODO: Neptuneでデータを確認してから
                             .authorId(null) // TODO: Neptuneでデータを確認してから
                             .status(null) // TODO: Neptuneでデータを確認してから
                             .build();
    }
}
