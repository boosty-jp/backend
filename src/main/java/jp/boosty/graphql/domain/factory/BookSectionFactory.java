package jp.boosty.graphql.domain.factory;

import jp.boosty.graphql.domain.domainmodel.book.section.BookSectionPages;
import jp.boosty.graphql.domain.domainmodel.book.section.BookSection;
import jp.boosty.graphql.domain.domainmodel.content.ContentId;
import jp.boosty.graphql.domain.domainmodel.content.ContentTitle;
import jp.boosty.graphql.infrastructure.datamodel.book.BookSectionEntity;

public class BookSectionFactory {
    public static BookSection make(BookSectionEntity entity) {

        BookSectionPages pages = BookSectionPagesFactory.make(entity.getPages());
        return BookSection.of(ContentId.of(entity.getId()), ContentTitle.of(entity.getTitle()), pages);
    }
}
