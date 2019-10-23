package co.jp.wever.graphql.application.converter.section;

import co.jp.wever.graphql.application.datamodel.response.query.section.SectionStatisticsResponse;
import co.jp.wever.graphql.domain.domainmodel.section.statistic.SectionStatistic;

public class SectionStatisticResponseConverter {
    public static SectionStatisticsResponse toSectionStatisticsResponse(SectionStatistic sectionStatistic) {
        return SectionStatisticsResponse.builder()
                                        .like(sectionStatistic.getLike().getValue())
                                        .build();
    }
}
