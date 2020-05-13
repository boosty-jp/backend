package jp.boosty.backend.infrastructure.datamodel.page;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageEntity {
    private String id;
    private String title;
    private String text;
    private boolean liked;
    private long likeCount;
    private boolean canPreview;
    private long createdDate;
    private long updatedDate;
}
