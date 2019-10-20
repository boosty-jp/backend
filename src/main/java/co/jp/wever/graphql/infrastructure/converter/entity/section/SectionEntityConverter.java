package co.jp.wever.graphql.infrastructure.converter.entity.section;

import co.jp.wever.graphql.domain.domainmodel.section.Section;
import co.jp.wever.graphql.infrastructure.datamodel.section.SectionEntity;

public class SectionEntityConverter {
    public static SectionEntity toSectionEntity(Section section) {
        return SectionEntity.builder()
                            .title(section.getTitle())
                            .texts(section.getTexts())
                            .number(section.getNumber())
                            .build();
    }
}
