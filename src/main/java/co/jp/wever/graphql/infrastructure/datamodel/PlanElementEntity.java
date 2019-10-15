package co.jp.wever.graphql.infrastructure.datamodel;

import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElementType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanElementEntity {
    private String targetId;
    private PlanElementType Type;
    private int number;
}
