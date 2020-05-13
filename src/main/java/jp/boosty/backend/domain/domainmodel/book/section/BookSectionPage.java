package jp.boosty.backend.domain.domainmodel.book.section;

import org.springframework.http.HttpStatus;

import java.util.Objects;

import jp.boosty.backend.domain.GraphQLCustomException;
import jp.boosty.backend.domain.domainmodel.content.ContentId;
import jp.boosty.backend.domain.domainmodel.content.ContentTitle;
import jp.boosty.backend.infrastructure.constant.GraphQLErrorMessage;

public class BookSectionPage {
    private ContentId id;
    private ContentTitle title;


    private BookSectionPage(ContentId id, ContentTitle title) {
        this.id = id;
        this.title = title;
    }

    public static BookSectionPage of(ContentId id, ContentTitle title) {
        if (Objects.isNull(id) || Objects.isNull(title)) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_PAGE_DATA.getString());
        }

        return new BookSectionPage(id, title);
    }

    public void publishValidation() {
        if (title.getValue().isEmpty()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.EMPTY_PAGE_TITLE.getString());
        }
    }
}
