package jp.boosty.graphql.infrastructure.datamodel.book;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookSectionPageEntity {
    private String id;
    private int number;
    private String title;
    private boolean canPreview;
}
