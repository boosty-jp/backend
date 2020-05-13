package jp.boosty.graphql.application.converter.book;

import jp.boosty.graphql.application.datamodel.response.query.book.PageResponse;
import jp.boosty.graphql.infrastructure.datamodel.book.BookSectionPageEntity;

public class PageResponseConverter {
    public static PageResponse toPageResponse(BookSectionPageEntity page) {
        return PageResponse.builder()
                           .id(page.getId())
                           .title(page.getTitle())
                           .canPreview(page.isCanPreview())
                           .number(page.getNumber())
                           .build();
    }
}
