package co.jp.wever.graphql.infrastructure.converter.entity.section;

import java.util.Map;

import co.jp.wever.graphql.domain.domainmodel.section.UpdateSection;
import co.jp.wever.graphql.infrastructure.converter.common.VertexConverter;
import co.jp.wever.graphql.infrastructure.datamodel.algolia.SectionSearchEntity;

public class SectionSearchEntityConverter {
    public static SectionSearchEntity toSectionSearchEntity(
        Map<String, Object> result, String authorId, long publishTime) {
        Map<Object, Object> base = (Map<Object, Object>) result.get("base");
        return SectionSearchEntity.builder()
                                  .objectID(VertexConverter.toId(base))
                                  .authorId(authorId)
                                  .like((long) result.get("liked"))
                                  .text(VertexConverter.toString("text", base))
                                  .title(VertexConverter.toString("title", base))
                                  .updateDate(publishTime)
                                  .build();
    }

    public static SectionSearchEntity toSectionSearchEntity(
        UpdateSection updateSection,
        long updateTime,
        String authorId) {
        return SectionSearchEntity.builder()
                                  .objectID(updateSection.getId())
                                  .title(updateSection.getTitle())
                                  .text(updateSection.getText())
                                  .like(0)
                                  .updateDate(updateTime)
                                  .authorId(authorId)
                                  .build();
    }
}
