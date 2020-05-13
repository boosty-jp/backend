package jp.boosty.backend.application.datamodel.response.query.book;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageResponse {
    private String id;
    private String title;
    private long number;
    private boolean canPreview;
}
