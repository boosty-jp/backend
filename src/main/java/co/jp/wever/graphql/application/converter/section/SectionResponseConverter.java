package co.jp.wever.graphql.application.converter.section;

import co.jp.wever.graphql.application.datamodel.response.query.section.SectionResponse;
import co.jp.wever.graphql.domain.domainmodel.section.Section;

public class SectionResponseConverter {
    public static SectionResponse toSectionResponse(Section section) {
        return SectionResponse.builder()
                              .id(section.getId().getValue())
                              .title(section.getTitle())
                              .texts(section.getTexts())
                              .number(section.getNumber())
                              .statistics(SectionStatisticResponseConverter.toSectionStatisticsResponse(section.getStatistic()))
                              .build();
    }
}
