package jp.boosty.backend.domain.factory;

import jp.boosty.backend.domain.domainmodel.book.section.BookSectionPages;
import jp.boosty.backend.domain.domainmodel.book.section.BookSection;
import jp.boosty.backend.domain.domainmodel.content.ContentId;
import jp.boosty.backend.domain.domainmodel.content.ContentTitle;
import jp.boosty.backend.infrastructure.datamodel.book.BookSectionEntity;

public class BookSectionFactory {
    public static BookSection make(BookSectionEntity entity) {

        BookSectionPages pages = BookSectionPagesFactory.make(entity.getPages());
        return BookSection.of(ContentId.of(entity.getId()), ContentTitle.of(entity.getTitle()), pages);
    }
}
