package co.jp.wever.graphql.infrastructure.datamodel.algolia;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionSearchEntity {
    private String objectID;
    private String title;
    private String text;
    private String authorId;
    private long like;
    private long updateDate;
}
