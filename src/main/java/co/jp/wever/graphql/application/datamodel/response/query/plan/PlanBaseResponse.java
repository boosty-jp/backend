package co.jp.wever.graphql.application.datamodel.response.query.plan;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanBaseResponse {
    private String id;
    private String title;
    private String description;
    private String imageUrl;
    private String status;
    private String createDate;
    private String updateDate;
}
