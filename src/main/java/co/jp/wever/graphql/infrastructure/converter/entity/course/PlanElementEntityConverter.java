package co.jp.wever.graphql.infrastructure.converter.entity.course;

import java.util.Map;

import co.jp.wever.graphql.infrastructure.constant.vertex.property.ArticleVertexProperty;
import co.jp.wever.graphql.infrastructure.converter.common.VertexConverter;
import co.jp.wever.graphql.infrastructure.datamodel.course.PlanElementEntity;

public class PlanElementEntityConverter {
    public static PlanElementEntity toPlanElementEntity(Map<Object, Object> vertex, Map<Object, Object> edge) {
        return PlanElementEntity.builder()
                                .id(VertexConverter.toId(vertex))
                                .imageUrl(VertexConverter.toString(ArticleVertexProperty.IMAGE_URL.getString(), vertex))
                                .number(VertexConverter.toNumber(edge))
                                .title(VertexConverter.toString(ArticleVertexProperty.TITLE.getString(), vertex))
                                .Type(VertexConverter.toLabel(vertex))
                                .build();
    }
}
