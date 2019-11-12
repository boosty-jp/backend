package co.jp.wever.graphql.application.datamodel.response.query.plan;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanElementStatisticsResponse {
    private long like;
    private long learned;
}
