package co.jp.wever.graphql.infrastructure.converter.entity.tag;

import java.util.Map;

import co.jp.wever.graphql.infrastructure.constant.vertex.property.TagVertexProperty;
import co.jp.wever.graphql.infrastructure.converter.common.VertexConverter;
import co.jp.wever.graphql.infrastructure.datamodel.tag.TagStatisticEntity;

public class TagStatisticEntityConverter {
    public static TagStatisticEntity toTagStatisticEntity(Map<Object, Object> result) {
        return TagStatisticEntity.builder()
                                 .id(VertexConverter.toId(result))
                                 .name(VertexConverter.toString(TagVertexProperty.NAME.getString(), result))
                                 .relatedCount(VertexConverter.toLong(TagVertexProperty.RELATED.getString(), result))
                                 .build();
    }
}
