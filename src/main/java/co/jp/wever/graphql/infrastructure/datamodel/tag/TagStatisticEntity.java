package co.jp.wever.graphql.infrastructure.datamodel.tag;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TagStatisticEntity {
    private String id;
    private String name;
    private long relatedCount;
}