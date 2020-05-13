package jp.boosty.graphql.domain.domainmodel.book.section;

import jp.boosty.graphql.domain.domainmodel.content.ContentId;

import org.springframework.http.HttpStatus;

import java.util.Objects;

import jp.boosty.graphql.domain.GraphQLCustomException;
import jp.boosty.graphql.domain.domainmodel.content.ContentTitle;
import jp.boosty.graphql.infrastructure.constant.GraphQLErrorMessage;

public class BookSection {
    private ContentId id;
    private ContentTitle title;
    private BookSectionPages pages;

    public BookSection(ContentId id, ContentTitle title, BookSectionPages pages) {
        this.id = id;
        this.title = title;
        this.pages = pages;
    }


    public static BookSection of(ContentId id, ContentTitle title, BookSectionPages pages) {
        if (Objects.isNull(title) || Objects.isNull(pages)) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.SECTION_NULL.getString());
        }

        return new BookSection(id, title, pages);
    }

    public ContentTitle getTitle() {
        return title;
    }

    public void publishValidation() {
        if (title.getValue().isEmpty()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.EMPTY_SECTION_TITLE.getString());
        }

        pages.publishValidation();
    }
}
