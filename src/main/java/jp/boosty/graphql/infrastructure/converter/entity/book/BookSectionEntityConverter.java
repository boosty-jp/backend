package jp.boosty.graphql.infrastructure.converter.entity.book;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jp.boosty.graphql.infrastructure.constant.vertex.property.BookVertexProperty;
import jp.boosty.graphql.infrastructure.constant.vertex.property.DateProperty;
import jp.boosty.graphql.infrastructure.converter.common.VertexConverter;
import jp.boosty.graphql.infrastructure.datamodel.book.BookSectionEntity;

public class BookSectionEntityConverter {
    public static BookSectionEntity toBookSectionEntity(Map<String, Object> result) {
        Map<Object, Object> baseResult = (Map<Object, Object>) result.get("sectionBase");
        List<Map<String, Object>> sectionContentResults = (List<Map<String, Object>>) result.get("sectionContents");
        return BookSectionEntity.builder()
                                .id(VertexConverter.toId(baseResult))
                                .title(VertexConverter.toString(BookVertexProperty.TITLE.getString(), baseResult))
                                .number((int) result.get("sectionNumber"))
                                .pages(sectionContentResults.stream()
                                                            .map(s -> BookSectionPageEntityConverter
                                                                                  .toBookSectionPageEntity(s))
                                                            .collect(Collectors.toList()))
                                .createdDate(VertexConverter.toLong(DateProperty.CREATE_TIME.getString(), baseResult))
                                .updatedDate(VertexConverter.toLong(DateProperty.UPDATE_TIME.getString(), baseResult))
                                .build();
    }

    public static BookSectionEntity toCourseSectionEntityForGuest(Map<String, Object> result) {
        Map<Object, Object> baseResult = (Map<Object, Object>) result.get("sectionBase");
        List<Map<String, Object>> sectionContentResults = (List<Map<String, Object>>) result.get("sectionContents");
        return BookSectionEntity.builder()
                                .id(VertexConverter.toId(baseResult))
                                .title(VertexConverter.toString(BookVertexProperty.TITLE.getString(), baseResult))
                                .number((int) result.get("sectionNumber"))
                                .pages(sectionContentResults.stream()
                                                            .map(s -> BookSectionPageEntityConverter
                                                                                  .toCourseSectionContentEntityForGuest(s))
                                                            .collect(Collectors.toList()))
                                .createdDate(VertexConverter.toLong(DateProperty.CREATE_TIME.getString(), baseResult))
                                .updatedDate(VertexConverter.toLong(DateProperty.UPDATE_TIME.getString(), baseResult))
                                .build();
    }

}
