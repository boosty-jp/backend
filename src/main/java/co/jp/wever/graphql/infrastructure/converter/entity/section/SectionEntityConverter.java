package co.jp.wever.graphql.infrastructure.converter.entity.section;

import co.jp.wever.graphql.domain.domainmodel.section.Section;
import co.jp.wever.graphql.infrastructure.datamodel.section.SectionEntity;

public class SectionEntityConverter {
    public static SectionEntity toSectionEntity(Section section) {
        return SectionEntity.builder()
                            .title(section.getTitle())
                            .text(section.getText())
                            .number(section.getNumber())
                            .build();
    }
}
