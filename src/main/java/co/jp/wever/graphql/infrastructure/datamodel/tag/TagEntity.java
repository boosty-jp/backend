package co.jp.wever.graphql.infrastructure.datamodel.tag;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TagEntity {
    private String tagId;
    private String name;
}
