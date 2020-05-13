package jp.boosty.backend.domain.domainmodel.book;

import jp.boosty.backend.domain.GraphQLCustomException;

import org.springframework.http.HttpStatus;

import java.util.Objects;

import jp.boosty.backend.infrastructure.constant.GraphQLErrorMessage;

public class BookTargets {
    private BookTargetLevel level;

    private BookTargetDescriptions descriptions;

    private BookTargets(
        BookTargetLevel level, BookTargetDescriptions descriptions) {
        this.level = level;
        this.descriptions = descriptions;
    }

    public static BookTargets of(BookTargetLevel level, BookTargetDescriptions descriptions) {
        if (Objects.isNull(level) || Objects.isNull(descriptions)) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_BOOK_DATA.getString());
        }

        return new BookTargets(level, descriptions);
    }

    public BookTargetLevel getLevel() {
        return level;
    }

    public BookTargetDescriptions getDescriptions() {
        return descriptions;
    }

}
