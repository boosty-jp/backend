package co.jp.wever.graphql.application.converter.section;

import co.jp.wever.graphql.application.datamodel.response.query.section.SectionResponse;
import co.jp.wever.graphql.application.datamodel.response.query.section.SectionStatisticsResponse;
import co.jp.wever.graphql.domain.domainmodel.section.FindSection;
import co.jp.wever.graphql.infrastructure.datamodel.section.SectionEntity;

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

    public static SectionResponse toSectionResponse(SectionEntity sectionEntity) {
        return SectionResponse.builder()
                              .id(sectionEntity.getId())
                              .title(sectionEntity.getTitle())
                              .text(sectionEntity.getText())
                              .number(sectionEntity.getNumber())
                              .authorId(sectionEntity.getAuthorId())
                              .liked(sectionEntity.isLiked())
                              .statistics(SectionStatisticsResponse.builder()
                                                                   .like(sectionEntity.getLikeCount())
                                                                   .build())
                              .build();
    }
}
