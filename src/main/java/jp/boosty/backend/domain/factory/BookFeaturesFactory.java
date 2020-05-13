package jp.boosty.backend.domain.factory;

import java.util.List;
import java.util.stream.Collectors;

import jp.boosty.backend.domain.domainmodel.book.BookFeature;
import jp.boosty.backend.domain.domainmodel.book.BookFeatures;

public class BookFeaturesFactory {
    public static BookFeatures make(List<String> features){
        List<BookFeature> featureList =
            features.stream().map(f -> BookFeature.of(f)).collect(Collectors.toList());

        return BookFeatures.of(featureList);
    }
}
