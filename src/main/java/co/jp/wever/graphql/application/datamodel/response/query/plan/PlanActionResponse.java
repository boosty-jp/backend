package co.jp.wever.graphql.application.datamodel.response.query.plan;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanActionResponse {
    private boolean liked;
    private boolean learning;
    private boolean learned;
}
