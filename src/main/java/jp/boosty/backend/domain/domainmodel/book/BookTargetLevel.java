package jp.boosty.backend.domain.domainmodel.book;

import jp.boosty.backend.domain.GraphQLCustomException;

import org.springframework.http.HttpStatus;

import jp.boosty.backend.infrastructure.constant.GraphQLErrorMessage;

public class BookTargetLevel {
    private int start;
    private int end;
    private final static int MIN_LEVEL = 0;
    private final static int MAX_LEVEL = 3;

    private BookTargetLevel(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public static BookTargetLevel of(int start, int end) {
        if (start < MIN_LEVEL || end < MIN_LEVEL) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_LEVEL.getString());
        }

        if (start > MAX_LEVEL || end > MAX_LEVEL) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_LEVEL.getString());
        }

        if (start > end) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_LEVEL.getString());
        }

        return new BookTargetLevel(start, end);
    }


    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

}
