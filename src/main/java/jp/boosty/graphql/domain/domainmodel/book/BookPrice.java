package jp.boosty.graphql.domain.domainmodel.book;

import org.springframework.http.HttpStatus;

import jp.boosty.graphql.domain.GraphQLCustomException;
import jp.boosty.graphql.infrastructure.constant.GraphQLErrorMessage;

public class BookPrice {
    private int value;
    private final static int FREE_PRICE = 0;
    private final static int MIN_PRICE = 50;
    private final static int MAX_PRICE = 50000;

    private BookPrice(int value) {
        this.value = value;
    }

    public static BookPrice of(int value) {
        if (value > MAX_PRICE) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.OVER_PRICE.getString());
        }

        if (value < MIN_PRICE && value != FREE_PRICE) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.LESS_PRICE.getString());
        }

        return new BookPrice(value);
    }

    public int getValue() {
        return value;
    }
}
