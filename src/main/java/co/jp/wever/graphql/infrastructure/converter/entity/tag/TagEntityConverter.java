package co.jp.wever.graphql.infrastructure.converter.entity.tag;

import java.util.Map;

import co.jp.wever.graphql.domain.domainmodel.tag.Tag;
import co.jp.wever.graphql.infrastructure.converter.common.VertexConverter;
import co.jp.wever.graphql.infrastructure.datamodel.tag.TagEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TagEntityConverter {
    public static TagEntity toTagEntity(Tag tag) {
        return TagEntity.builder().tagId(tag.getId().getValue()).name(tag.getName().getValue()).build();
    }

    public static TagEntity toTagEntity(Map<Object, Object> tagVertex) {
        return TagEntity.builder()
                        .name(VertexConverter.toString("name", tagVertex))
                        .tagId(VertexConverter.toId(tagVertex))
                        .build();
    }
}
