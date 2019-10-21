package co.jp.wever.graphql.application.datamodel.response.query.section;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionResponse {
    private String id;
    private String title;
    private int number;
    private String text;
    private SectionStatisticsResponse statistics;
}
