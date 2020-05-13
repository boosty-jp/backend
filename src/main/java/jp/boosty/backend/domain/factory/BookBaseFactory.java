package jp.boosty.backend.domain.factory;

import jp.boosty.backend.application.datamodel.request.book.BookBaseInput;
import jp.boosty.backend.domain.domainmodel.content.ContentTitle;
import jp.boosty.backend.domain.domainmodel.book.BookBase;
import jp.boosty.backend.domain.domainmodel.book.BookDescription;
import jp.boosty.backend.domain.domainmodel.book.BookPrice;

public class BookBaseFactory {
    public static BookBase make(BookBaseInput bookBaseInput) {
        return BookBase.of(ContentTitle.of(bookBaseInput.getTitle()),
                           BookDescription.of(bookBaseInput.getDescription()),
                           BookPrice.of(bookBaseInput.getPrice()));
    }
}
