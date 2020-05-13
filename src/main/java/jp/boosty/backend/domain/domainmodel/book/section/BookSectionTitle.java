package jp.boosty.backend.domain.domainmodel.book.section;

import jp.boosty.backend.domain.GraphQLCustomException;

import org.springframework.http.HttpStatus;

import jp.boosty.backend.infrastructure.constant.GraphQLErrorMessage;

import io.netty.util.internal.StringUtil;

public class BookSectionTitle {
    private String title;
    private final static int MAX_WORD_COUNT = 40;


    private BookSectionTitle(String title) {
        this.title = title;
    }

    public static BookSectionTitle of(String name) {
        if (StringUtil.isNullOrEmpty(name)) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.EMPTY_SECTION_TITLE.getString());
        }

        if (name.length() > MAX_WORD_COUNT) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.OVER_SECTION_TITLE.getString());
        }

        return new BookSectionTitle(name);
    }

    public String getTitle() {
        return title;
    }
}
