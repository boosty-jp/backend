package jp.boosty.backend.application.datamodel.response.query.page;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageResponse {
    private String id;
    private String title;
    private String text;
    private boolean liked;
    private long likeCount;
    private boolean canPreview;
    private String createDate;
    private String updateDate;
}
