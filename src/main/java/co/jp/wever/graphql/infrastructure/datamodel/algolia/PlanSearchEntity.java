package co.jp.wever.graphql.infrastructure.datamodel.algolia;


import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanSearchEntity {
    private String objectID;
    private String title;
    private String description;
    private String imageUrl;
    private String authorId;
    private List<String> tags;
    private long like;
    private long learned;
    private long updateDate;
}
