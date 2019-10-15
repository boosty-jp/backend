package co.jp.wever.graphql.infrastructure.datamodel;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanEntity {
    private String id;
    private String title;
    private String image;
    private String description;
    private Boolean publish;
    private Boolean deleted;
}
