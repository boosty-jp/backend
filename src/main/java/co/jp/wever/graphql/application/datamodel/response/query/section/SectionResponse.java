package co.jp.wever.graphql.application.datamodel.response.query.section;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionResponse {
    private String id;
    private String title;
    private int number;
    private List<String> texts;
    private SectionStatisticsResponse statistics;
}
