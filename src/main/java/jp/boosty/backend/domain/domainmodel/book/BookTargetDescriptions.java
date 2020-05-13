package jp.boosty.backend.domain.domainmodel.book;

import jp.boosty.backend.domain.GraphQLCustomException;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Objects;

import jp.boosty.backend.infrastructure.constant.GraphQLErrorMessage;

public class BookTargetDescriptions {
    private List<BookTargetDescription> descriptions;
    private final static int MAX_DESCRIPTION_SIZE = 6;


    private BookTargetDescriptions(List<BookTargetDescription> descriptions) {
        this.descriptions = descriptions;
    }

    public static BookTargetDescriptions of(List<BookTargetDescription> descriptions) {
        if (Objects.isNull(descriptions)) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.EMPTY_TARGET_DESCRIPTION_WORD.getString());
        }

        if (descriptions.size() == 0) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.EMPTY_TARGET_DESCRIPTION_WORD.getString());
        }

        if (descriptions.size() > MAX_DESCRIPTION_SIZE) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.OVER_TARGET_DESCRIPTION_COUNT.getString());
        }

        return new BookTargetDescriptions(descriptions);
    }

    public List<BookTargetDescription> getDescriptions() {
        return descriptions;
    }

}
