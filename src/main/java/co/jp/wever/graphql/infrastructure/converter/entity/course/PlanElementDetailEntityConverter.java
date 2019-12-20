package co.jp.wever.graphql.infrastructure.converter.entity.course;

import java.util.Map;

import co.jp.wever.graphql.infrastructure.converter.common.VertexConverter;
import co.jp.wever.graphql.infrastructure.datamodel.course.PlanElementActionEntity;
import co.jp.wever.graphql.infrastructure.datamodel.course.PlanElementDetailEntity;
import co.jp.wever.graphql.infrastructure.datamodel.course.PlanElementStatisticsEntity;

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

    public static PlanElementDetailEntity toPlanElementDetailEntityForGuest(Map<String, Object> result) {
        Map<Object, Object> baseResult = (Map<Object, Object>) result.get("base");
        Map<Object, Object> edgeResult = (Map<Object, Object>) result.get("edge");

        boolean userLiked = false;
        boolean userLearned = false;
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
