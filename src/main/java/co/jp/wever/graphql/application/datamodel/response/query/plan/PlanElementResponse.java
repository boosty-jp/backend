package co.jp.wever.graphql.application.datamodel.response.query.plan;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class PlanElementResponse {
    private String id;
    private String title;
    private String imageUrl;
    private String type;
    private int number;
}
