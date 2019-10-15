package co.jp.wever.graphql.infrastructure.datamodel;

import co.jp.wever.graphql.infrastructure.constant.PlanElementType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanElement {
    private String targetId;
    private PlanElementType Type;
    private int number;
}
