package jp.boosty.graphql.domain.factory;

import jp.boosty.graphql.application.datamodel.request.book.BookBaseInput;
import jp.boosty.graphql.domain.domainmodel.content.ContentTitle;
import jp.boosty.graphql.domain.domainmodel.book.BookBase;
import jp.boosty.graphql.domain.domainmodel.book.BookDescription;
import jp.boosty.graphql.domain.domainmodel.book.BookPrice;

public class BookBaseFactory {
    public static BookBase make(BookBaseInput bookBaseInput) {
        return BookBase.of(ContentTitle.of(bookBaseInput.getTitle()),
                           BookDescription.of(bookBaseInput.getDescription()),
                           BookPrice.of(bookBaseInput.getPrice()));
    }
}
