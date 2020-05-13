package jp.boosty.backend.infrastructure.converter.entity.book;

import jp.boosty.backend.infrastructure.constant.vertex.property.BookFeatureProperty;
import jp.boosty.backend.infrastructure.converter.common.VertexConverter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class BookFeaturesEntityConverter {
    public static List<String> toBookFeatures(List<Map<Object, Object>> features) {
        if (Objects.isNull(features)) {
            return Collections.emptyList();
        }

        return features.stream()
                       .sorted(Comparator.comparingInt(v -> VertexConverter.toInt(BookFeatureProperty.NUMBER.getString(),
                                                                                  v)))
                       .map(f -> VertexConverter.toString(BookFeatureProperty.TEXT.getString(), f))
                       .collect(Collectors.toList());
    }
}
