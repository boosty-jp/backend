package co.jp.wever.graphql.application.datamodel.response.query.section;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionResponse {
    private String id;
    private String title;
    private String text;
    private long number;
    private String authorId;
    private SectionStatisticsResponse statistics;
    private boolean liked;
}
