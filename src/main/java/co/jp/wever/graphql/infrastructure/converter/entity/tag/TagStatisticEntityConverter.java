package co.jp.wever.graphql.infrastructure.converter.entity.tag;

import java.util.Map;

import co.jp.wever.graphql.infrastructure.constant.vertex.property.TagVertexProperty;
import co.jp.wever.graphql.infrastructure.converter.common.VertexConverter;
import co.jp.wever.graphql.infrastructure.datamodel.tag.TagStatisticEntity;

public class TagStatisticEntityConverter {
    public static TagStatisticEntity toTagStatisticEntity(Map<String, Object> result) {
        Map<Object, Object> tagVertex = (Map<Object, Object>) result.get("tag");
        return TagStatisticEntity.builder()
                                 .id(VertexConverter.toId(tagVertex))
                                 .name(VertexConverter.toString(TagVertexProperty.NAME.getString(), tagVertex))
                                 .relatedCount((long) result.get("count"))
                                 .build();
    }
}
