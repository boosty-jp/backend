package co.jp.wever.graphql.infrastructure.datamodel.plan;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanElementActionEntity {
    private boolean liked;
    private boolean learned;
}
