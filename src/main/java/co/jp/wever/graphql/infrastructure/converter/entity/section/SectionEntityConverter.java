package co.jp.wever.graphql.infrastructure.converter.entity.section;

import java.util.Map;

import co.jp.wever.graphql.domain.domainmodel.section.Section;
import co.jp.wever.graphql.infrastructure.converter.common.VertexConverter;
import co.jp.wever.graphql.infrastructure.datamodel.section.SectionEntity;

public class SectionEntityConverter {
    public static SectionEntity toSectionEntity(Section section, String sectionId) {
        return SectionEntity.builder()
                            .id(sectionId)
                            .title(section.getTitle())
                            .text(section.getText())
                            .number(section.getNumber())
                            .build();
    }

    public static SectionEntity toSectionEntity(Section section) {
        return SectionEntity.builder()
                            .title(section.getTitle())
                            .text(section.getText())
                            .number(section.getNumber())
                            .build();
    }

    public static SectionEntity toSectionEntity(Map<String, Object> resultMap) {
        Map<Object, Object> base = (Map<Object, Object>) resultMap.get("base");

        return SectionEntity.builder()
                            .id(VertexConverter.toId(base))
                            .authorId((String) resultMap.get("author"))
                            .number((int) resultMap.get("number"))
                            .status((String) resultMap.get("status"))
                            .title(VertexConverter.toString("title", base))
                            .text(VertexConverter.toString("text", base))
                            .likeCount(Math.toIntExact((long) resultMap.get("like")))
                            .build();
    }
}
