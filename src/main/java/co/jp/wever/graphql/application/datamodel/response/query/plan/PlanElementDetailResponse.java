package co.jp.wever.graphql.application.datamodel.response.query.plan;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanElementDetailResponse {
    private String id;
    private String title;
    private String imageUrl;
    private String type;
    private int number;
    private PlanElementActionResponse action;
    private PlanElementStatisticsResponse statistics;

}
