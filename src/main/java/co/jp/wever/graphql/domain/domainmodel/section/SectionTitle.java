package co.jp.wever.graphql.domain.domainmodel.section;

import org.springframework.http.HttpStatus;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class SectionTitle {
    private final static int MIN_WORD_COUNT = 1;
    private final static int MAX_WORD_COUNT = 100;
    private String value;

    private SectionTitle(String value) {
        this.value = value;
    }

    public static SectionTitle of(String value) {
        if (value.length() < MIN_WORD_COUNT) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.EMPTY_TITLE.getString());
        }

        if (value.length() > MAX_WORD_COUNT) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.TITLE_OVER.getString());
        }

        return new SectionTitle(value);
    }

    public String getValue() {
        return this.value;
    }
}
