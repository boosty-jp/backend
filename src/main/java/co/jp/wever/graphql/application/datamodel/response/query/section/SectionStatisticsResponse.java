package co.jp.wever.graphql.application.datamodel.response.query.section;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionStatisticsResponse {
    private int like;
}
