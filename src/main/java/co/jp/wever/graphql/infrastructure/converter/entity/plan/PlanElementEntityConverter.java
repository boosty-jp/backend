package co.jp.wever.graphql.infrastructure.converter.entity.plan;

import java.util.Map;

import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElementType;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.ArticleVertexProperty;
import co.jp.wever.graphql.infrastructure.converter.common.VertexConverter;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanElementEntity;

public class PlanElementEntityConverter {
    public static PlanElementEntity toPlanElementEntity(Map<Object, Object> result, int number) {
        return PlanElementEntity.builder()
                                .targetId(VertexConverter.toId(result))
                                .imageUrl(VertexConverter.toString(ArticleVertexProperty.IMAGE_URL.getString(), result))
                                .number(number)
                                .title(VertexConverter.toString(ArticleVertexProperty.TITLE.getString(), result))
                                .Type(PlanElementType.fromString(VertexConverter.toLabel(result)))
                                .build();
    }
}
