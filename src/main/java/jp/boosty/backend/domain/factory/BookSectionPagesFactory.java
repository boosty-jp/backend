package jp.boosty.backend.domain.factory;

import jp.boosty.backend.domain.domainmodel.book.section.BookSectionPage;
import jp.boosty.backend.domain.domainmodel.book.section.BookSectionPages;

import java.util.List;
import java.util.stream.Collectors;

import jp.boosty.backend.domain.domainmodel.content.ContentId;
import jp.boosty.backend.domain.domainmodel.content.ContentTitle;
import jp.boosty.backend.infrastructure.datamodel.book.BookSectionPageEntity;

public class BookSectionPagesFactory {
    public static BookSectionPages make(List<BookSectionPageEntity> entities) {
        List<BookSectionPage> pages = entities.stream()
                                              .map(e -> BookSectionPage.of(ContentId.of(e.getId()),
                                                                           ContentTitle.of(e.getTitle())))
                                              .collect(Collectors.toList());
        return BookSectionPages.of(pages);
    }
}
