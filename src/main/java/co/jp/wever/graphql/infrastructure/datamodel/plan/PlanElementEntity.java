package co.jp.wever.graphql.infrastructure.datamodel.plan;

import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElementType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanElementEntity {
    private String targetId;
    private String title;
    private PlanElementType Type;
    private int number;
}
