package jp.boosty.graphql.infrastructure.datamodel.book;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookSectionEntity {
    private String id;
    private int number;
    private String title;
    private List<BookSectionPageEntity> pages;
    private long createdDate;
    private long updatedDate;
}
