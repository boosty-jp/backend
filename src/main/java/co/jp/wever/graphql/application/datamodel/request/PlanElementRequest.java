package co.jp.wever.graphql.application.datamodel.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanElementRequest {
    private String targetId;
    private String type;
    private int number;
}
