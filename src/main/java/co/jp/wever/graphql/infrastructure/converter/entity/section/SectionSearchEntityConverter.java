package co.jp.wever.graphql.infrastructure.converter.entity.section;

import java.util.Map;

import co.jp.wever.graphql.infrastructure.converter.common.VertexConverter;
import co.jp.wever.graphql.infrastructure.datamodel.algolia.SectionSearchEntity;

public class SectionSearchEntityConverter {
    public static SectionSearchEntity toSectionSearchEntity(
        Map<String, Object> result,
        String authorId,
        long publishTime) {
        Map<Object, Object> base = (Map<Object, Object>) result.get("base");
        return SectionSearchEntity.builder()
                                  .objectID(VertexConverter.toId(base))
                                  .authorId(authorId)
                                  .like((long) result.get("like"))
                                  .text(VertexConverter.toString("text", base))
                                  .title(VertexConverter.toString("title", base))
                                  .updateDate(publishTime)
                                  .build();
    }
}
