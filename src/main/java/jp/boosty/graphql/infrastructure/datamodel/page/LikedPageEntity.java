package jp.boosty.graphql.infrastructure.datamodel.page;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LikedPageEntity {
    private String id;
    private String title;
    private String bookId;
    private String bookTitle;
    private String bookImage;
}
