package co.jp.wever.graphql.infrastructure.datamodel.plan;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanElementEntity {
    private String id;
    private String title;
    private String imageUrl;
    private String Type;
    private int number;
}
