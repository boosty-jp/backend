package jp.boosty.backend.infrastructure.converter.entity.book;

import jp.boosty.backend.infrastructure.constant.vertex.property.BookTargetDescriptionProperty;
import jp.boosty.backend.infrastructure.converter.common.VertexConverter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class BookTargetDescriptionEntityConverter {
    public static List<String> toBookTargets(List<Map<Object, Object>> targetDescriptions) {
        if (Objects.isNull(targetDescriptions)) {
            return Collections.emptyList();
        }

        return targetDescriptions.stream()
                                 .sorted(Comparator.comparingInt(v -> VertexConverter.toInt(
                                     BookTargetDescriptionProperty.NUMBER.getString(),
                                     v)))
                                 .map(f -> VertexConverter.toString(BookTargetDescriptionProperty.TEXT.getString(), f))
                                 .collect(Collectors.toList());
    }
}
