package co.jp.wever.graphql.application.datamodel.response.query.plan;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanElementActionResponse {
    private boolean liked;
    private boolean learned;
}
