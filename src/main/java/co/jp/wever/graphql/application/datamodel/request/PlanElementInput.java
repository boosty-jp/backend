package co.jp.wever.graphql.application.datamodel.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanElementInput {
    private String targetId;
    private String title;
    private String imageUrl;
    private String type;
    private int number;
}
