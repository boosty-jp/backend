package jp.boosty.backend.infrastructure.converter.entity.book;

import java.util.List;
import java.util.Map;

import jp.boosty.backend.infrastructure.constant.vertex.property.BookVertexProperty;
import jp.boosty.backend.infrastructure.constant.vertex.property.DateProperty;
import jp.boosty.backend.infrastructure.converter.common.VertexConverter;
import jp.boosty.backend.infrastructure.datamodel.book.BookBaseEntity;

public class BookBaseEntityConverter {
    public static BookBaseEntity toBookBaseEntity(
        Map<Object, Object> courseVertex,
        String status,
        List<Map<Object, Object>> features,
        List<Map<Object, Object>> targetDescriptions) {
        return BookBaseEntity.builder()
                             .id(VertexConverter.toId(courseVertex))
                             .title(VertexConverter.toString(BookVertexProperty.TITLE.getString(), courseVertex))
                             .imageUrl(VertexConverter.toString(BookVertexProperty.IMAGE_URL.getString(), courseVertex))
                             .description(VertexConverter.toString(BookVertexProperty.DESCRIPTION.getString(),
                                                                   courseVertex))
                             .features(BookFeaturesEntityConverter.toBookFeatures(features))
                             .price(VertexConverter.toInt(BookVertexProperty.PRICE.getString(), courseVertex))
                             .levelStart(VertexConverter.toInt(BookVertexProperty.LEVEL_START.getString(),
                                                               courseVertex))
                             .levelEnd(VertexConverter.toInt(BookVertexProperty.LEVEL_END.getString(), courseVertex))
                             .targetDescriptions(BookTargetDescriptionEntityConverter.toBookTargets(targetDescriptions))
                             .status(status)
                             .createdDate(VertexConverter.toLong(DateProperty.CREATE_TIME.getString(), courseVertex))
                             .updatedDate(VertexConverter.toLong(DateProperty.UPDATE_TIME.getString(), courseVertex))
                             .build();
    }

    public static BookBaseEntity toBookBaseEntityForList(
        Map<Object, Object> courseVertex, String status) {
        return BookBaseEntity.builder()
                             .id(VertexConverter.toId(courseVertex))
                             .title(VertexConverter.toString(BookVertexProperty.TITLE.getString(), courseVertex))
                             .imageUrl(VertexConverter.toString(BookVertexProperty.IMAGE_URL.getString(), courseVertex))
                             .price(VertexConverter.toInt(BookVertexProperty.PRICE.getString(), courseVertex))
                             .levelStart(VertexConverter.toInt(BookVertexProperty.LEVEL_START.getString(),
                                                               courseVertex))
                             .levelEnd(VertexConverter.toInt(BookVertexProperty.LEVEL_END.getString(), courseVertex))
                             .status(status)
                             .createdDate(VertexConverter.toLong(DateProperty.CREATE_TIME.getString(), courseVertex))
                             .updatedDate(VertexConverter.toLong(DateProperty.UPDATE_TIME.getString(), courseVertex))
                             .build();
    }

    public static BookBaseEntity toReferenceCourseBaseEntity(Map<Object, Object> courseVertex) {
        return BookBaseEntity.builder()
                             .id(VertexConverter.toId(courseVertex))
                             .title(VertexConverter.toString(BookVertexProperty.TITLE.getString(), courseVertex))
                             .imageUrl(VertexConverter.toString(BookVertexProperty.IMAGE_URL.getString(), courseVertex))
                             .build();
    }
}
