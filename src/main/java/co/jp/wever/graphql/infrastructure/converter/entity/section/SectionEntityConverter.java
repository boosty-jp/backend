package co.jp.wever.graphql.infrastructure.converter.entity.section;

import java.util.Map;

import co.jp.wever.graphql.domain.domainmodel.section.FindSection;
import co.jp.wever.graphql.infrastructure.converter.common.VertexConverter;
import co.jp.wever.graphql.infrastructure.datamodel.section.SectionEntity;

public class SectionEntityConverter {
    public static SectionEntity toSectionEntity(FindSection findSection, String sectionId) {
        return SectionEntity.builder()
                            .id(sectionId)
                            .title(findSection.getTitle())
                            .text(findSection.getText())
                            .number(findSection.getNumber())
                            .build();
    }

    public static SectionEntity toSectionEntity(FindSection findSection) {
        return SectionEntity.builder()
                            .title(findSection.getTitle())
                            .text(findSection.getText())
                            .number(findSection.getNumber())
                            .build();
    }

    public static SectionEntity toSectionEntity(Map<String, Object> resultMap, boolean noLike) {
        Map<Object, Object> base = (Map<Object, Object>) resultMap.get("base");

        boolean liked = noLike ? false : (boolean) resultMap.get("liked");

        return SectionEntity.builder()
                            .id(VertexConverter.toId(base))
                            .authorId((String) resultMap.get("author"))
                            .number((long) resultMap.get("number"))
                            .status((String) resultMap.get("status"))
                            .title(VertexConverter.toString("title", base))
                            .text(VertexConverter.toString("text", base))
                            .likeCount(Math.toIntExact((long) resultMap.get("like")))
                            .liked(liked)
                            .build();
    }
}
