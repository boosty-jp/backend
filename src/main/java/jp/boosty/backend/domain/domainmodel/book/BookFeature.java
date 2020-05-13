package jp.boosty.backend.domain.domainmodel.book;

import jp.boosty.backend.domain.GraphQLCustomException;

import org.springframework.http.HttpStatus;

import jp.boosty.backend.infrastructure.constant.GraphQLErrorMessage;

import io.netty.util.internal.StringUtil;

public class BookFeature {
    private String feature;
    private final static int MAX_WORD_COUNT = 50;

    private BookFeature(String feature) {
        this.feature = feature;
    }

    public static BookFeature of(String value) {
        if (StringUtil.isNullOrEmpty(value)) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.EMPTY_FEATURE_WORD.getString());
        }

        if (value.length() > MAX_WORD_COUNT) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.OVER_FEATURE_WORD.getString());
        }

        return new BookFeature(value);
    }

    public String getFeature() {
        return feature;
    }
}
