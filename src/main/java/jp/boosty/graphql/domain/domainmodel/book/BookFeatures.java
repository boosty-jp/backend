package jp.boosty.graphql.domain.domainmodel.book;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Objects;

import jp.boosty.graphql.domain.GraphQLCustomException;
import jp.boosty.graphql.infrastructure.constant.GraphQLErrorMessage;

public class BookFeatures {
    private List<BookFeature> features;
    private static final int MAX_FEATURE_COUNT = 6;

    private BookFeatures(List<BookFeature> features) {
        this.features = features;
    }

    public static BookFeatures of(List<BookFeature> features) {
        if (Objects.isNull(features)) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_BOOK_DATA.getString());
        }

        if (features.size() == 0) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.EMPTY_FEATURE_WORD.getString());
        }

        if (features.size() > MAX_FEATURE_COUNT) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.OVER_FEATURE_COUNT.getString());
        }

        return new BookFeatures(features);
    }

    public List<BookFeature> getFeatures() {
        return features;
    }
}
