package co.jp.wever.graphql.application.converter.section;

import co.jp.wever.graphql.application.datamodel.response.query.section.SectionResponse;
import co.jp.wever.graphql.domain.domainmodel.section.Section;

public class SectionResponseConverter {
    public static SectionResponse toSectionResponse(Section section) {
        return SectionResponse.builder()
                              .id(section.getId().getValue())
                              .title(section.getTitle())
                              .text(section.getText())
                              .number(section.getNumber())
                              .authorId(section.getAuthorId())
                              .liked(section.isLiked())
                              .statistics(SectionStatisticResponseConverter.toSectionStatisticsResponse(section.getStatistic()))
                              .build();
    }
}
