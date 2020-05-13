package jp.boosty.backend.infrastructure.datamodel.algolia;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageSearchEntity {
    private String objectID;
    private String bookId;
    private String title;
    private String text;
    private long updateDate;
}
