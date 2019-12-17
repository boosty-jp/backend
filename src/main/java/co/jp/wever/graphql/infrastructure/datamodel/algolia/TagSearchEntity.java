package co.jp.wever.graphql.infrastructure.datamodel.algolia;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TagSearchEntity {
    private String objectID;
    private String name;
    private int relatedCount;
}