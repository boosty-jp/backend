package co.jp.wever.graphql.infrastructure.converter.entity.section;

import java.util.Map;

import co.jp.wever.graphql.infrastructure.datamodel.section.SectionNumberEntity;

public class SectionNumberEntityConverter {
    public static SectionNumberEntity toSectionNumberEntity(Map<String, Object> resultMap) {
        return SectionNumberEntity.builder()
                                  .id((String) resultMap.get("id"))
                                  .number((int) resultMap.get("number"))
                                  .build();
    }
}
