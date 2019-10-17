package co.jp.wever.graphql.infrastructure.datamodel.plan;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanBaseEntity {
    private String id;
    private String title;
    private String description;
    private String imageUrl;
    private List<String> tagIds;
    private String status;
    private String authorId;
}
