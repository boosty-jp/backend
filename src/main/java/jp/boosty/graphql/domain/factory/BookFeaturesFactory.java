package jp.boosty.graphql.domain.factory;

import java.util.List;
import java.util.stream.Collectors;

import jp.boosty.graphql.domain.domainmodel.book.BookFeature;
import jp.boosty.graphql.domain.domainmodel.book.BookFeatures;

public class BookFeaturesFactory {
    public static BookFeatures make(List<String> features){
        List<BookFeature> featureList =
            features.stream().map(f -> BookFeature.of(f)).collect(Collectors.toList());

        return BookFeatures.of(featureList);
    }
}
