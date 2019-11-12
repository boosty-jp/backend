package co.jp.wever.graphql.infrastructure.converter.entity.plan;

import java.util.Map;

import co.jp.wever.graphql.infrastructure.constant.vertex.property.PlanVertexProperty;
import co.jp.wever.graphql.infrastructure.converter.common.VertexConverter;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanElementActionEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanElementDetailEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanElementStatisticsEntity;

public class PlanElementDetailEntityConverter {
    public static PlanElementDetailEntity toPlanElementDetailEntity(Map<String, Object> result) {
        Map<Object, Object> baseResult = (Map<Object, Object>) result.get("base");
        Map<Object, Object> edgeResult = (Map<Object, Object>) result.get("edge");

        boolean userLiked = (boolean) result.get("userLiked");
        boolean userLearned = (boolean) result.get("userLearned");
        long like = (long) result.get("like");
        long learning = (long) result.get("learned");

        PlanElementStatisticsEntity planElementStatisticsEntity =
            PlanElementStatisticsEntity.builder().learned(learning).like(like).build();

        PlanElementActionEntity planElementActionEntity =
            PlanElementActionEntity.builder().learned(userLearned).liked(userLiked).build();

        return PlanElementDetailEntity.builder()
                                      .id(VertexConverter.toId(baseResult))
                                      .imageUrl(VertexConverter.toString(PlanVertexProperty.IMAGE_URL.getString(),
                                                                         baseResult))
                                      .number(VertexConverter.toNumber(edgeResult))
                                      .action(planElementActionEntity)
                                      .statistics(planElementStatisticsEntity)
                                      .title(VertexConverter.toString(PlanVertexProperty.TITLE.getString(), baseResult))
                                      .Type(VertexConverter.toLabel(baseResult))
                                      .build();
    }
}
