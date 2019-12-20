package co.jp.wever.graphql.infrastructure.datamodel.course;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanElementDetailEntity {
    private String id;
    private String title;
    private String imageUrl;
    private String Type;
    private int number;
    private PlanElementActionEntity action;
    private PlanElementStatisticsEntity statistics;
}
