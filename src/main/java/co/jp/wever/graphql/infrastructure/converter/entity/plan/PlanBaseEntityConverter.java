package co.jp.wever.graphql.infrastructure.converter.entity.plan;

import java.util.Map;

import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanBase;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.PlanVertexProperty;
import co.jp.wever.graphql.infrastructure.converter.common.VertexConverter;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanBaseEntity;

public class PlanBaseEntityConverter {
    public static PlanBaseEntity toPlanBaseEntity(PlanBase planBase) {
        return PlanBaseEntity.builder()
                             .title(planBase.getTitle())
                             .description(planBase.getDescription())
                             .imageUrl(planBase.getImageUrl())
                             .status(planBase.getStatus().getString())
                             .build();
    }

    public static PlanBaseEntity toPlanBaseEntity(Map<Object, Object> planVertex, String status) {
        return PlanBaseEntity.builder()
                             .id(VertexConverter.toId(planVertex))
                             .title(VertexConverter.toString(PlanVertexProperty.TITLE.getString(), planVertex))
                             .imageUrl(VertexConverter.toString(PlanVertexProperty.IMAGE_URL.getString(), planVertex))
                             .description(VertexConverter.toString(PlanVertexProperty.DESCRIPTION.getString(),
                                                                   planVertex))
                             .createdDate(VertexConverter.toLong(PlanVertexProperty.CREATED_TIME.getString(),
                                                                 planVertex))
                             .updatedDate(VertexConverter.toLong(PlanVertexProperty.UPDATED_TIME.getString(),
                                                                 planVertex))

                             .status(status)
                             .build();
    }
}
