package jp.boosty.graphql.domain.domainmodel.book.section;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Objects;

import jp.boosty.graphql.domain.GraphQLCustomException;
import jp.boosty.graphql.infrastructure.constant.GraphQLErrorMessage;

public class BookSectionPages {
    private List<BookSectionPage> pages;

    private BookSectionPages(List<BookSectionPage> pages) {
        this.pages = pages;
    }

    public static BookSectionPages of(List<BookSectionPage> pages) {
        if (Objects.isNull(pages)) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_BOOK_DATA.getString());
        }

        return new BookSectionPages(pages);
    }

    public void publishValidation() {
        if (pages.size() == 0) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.EMPTY_PAGE_SECTION.getString());
        }

        if (pages.size() > 20) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.PAGE_SIZE_OVER.getString());
        }

        pages.forEach(p -> p.publishValidation());
    }
}
