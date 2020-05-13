package jp.boosty.graphql.application.datamodel.response.query.page;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LikedPageResponse {
    private String id;
    private String title;
    private String bookId;
    private String bookTitle;
    private String bookImage;
}
