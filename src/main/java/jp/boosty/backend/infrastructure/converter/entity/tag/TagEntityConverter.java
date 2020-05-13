package jp.boosty.backend.infrastructure.converter.entity.tag;

import jp.boosty.backend.infrastructure.constant.edge.EdgeLabel;
import jp.boosty.backend.infrastructure.converter.common.VertexConverter;
import jp.boosty.backend.infrastructure.datamodel.tag.TagEntity;

import java.util.Map;

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
