package co.jp.wever.graphql.application.converter.section;

import co.jp.wever.graphql.application.datamodel.response.query.section.SectionResponse;
import co.jp.wever.graphql.domain.domainmodel.section.FindSection;

public class SectionResponseConverter {
    public static SectionResponse toSectionResponse(FindSection findSection) {
        return SectionResponse.builder()
                              .id(findSection.getId().getValue())
                              .title(findSection.getTitle())
                              .text(findSection.getText())
                              .number(findSection.getNumber())
                              .authorId(findSection.getAuthorId())
                              .liked(findSection.isLiked())
                              .statistics(SectionStatisticResponseConverter.toSectionStatisticsResponse(findSection.getStatistic()))
                              .build();
    }
}
