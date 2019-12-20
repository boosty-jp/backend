package co.jp.wever.graphql.infrastructure.converter.entity.tag;

import java.util.Map;

import co.jp.wever.graphql.infrastructure.constant.edge.EdgeLabel;
import co.jp.wever.graphql.infrastructure.converter.common.VertexConverter;
import co.jp.wever.graphql.infrastructure.datamodel.tag.TagEntity;

public class TagEntityConverter {
    public static TagEntity toTagEntity(Map<Object, Object> tagVertex) {
        return TagEntity.builder()
                        .name(VertexConverter.toString("name", tagVertex))
                        .id(VertexConverter.toId(tagVertex))
                        .relatedCount(0)
                        .build();
    }

    public static TagEntity toTagEntityWithRelatedCount(Map<Object, Object> tagVertex) {
        return TagEntity.builder()
                        .name(VertexConverter.toString("name", tagVertex))
                        .id(VertexConverter.toId(tagVertex))
                        .relatedCount(VertexConverter.toLong(EdgeLabel.RELATED_TO.getString(), tagVertex))
                        .build();
    }
}
