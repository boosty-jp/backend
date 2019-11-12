package co.jp.wever.graphql.infrastructure.datamodel.section;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionEntity {
    private String id;
    private String title;
    private long number;
    private String text;
    private String authorId;
    private String status;
    private int likeCount;
    private boolean liked;
}
