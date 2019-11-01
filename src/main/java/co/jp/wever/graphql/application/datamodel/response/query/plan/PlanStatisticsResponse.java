package co.jp.wever.graphql.application.datamodel.response.query.plan;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanStatisticsResponse {
    private long like;
    private long learning;
    private long learned;
}
