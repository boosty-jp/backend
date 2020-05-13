package jp.boosty.graphql.infrastructure.converter.entity.book;

import java.util.Map;

import jp.boosty.graphql.infrastructure.constant.vertex.property.BookVertexProperty;
import jp.boosty.graphql.infrastructure.constant.vertex.property.PageVertexProperty;
import jp.boosty.graphql.infrastructure.converter.common.VertexConverter;
import jp.boosty.graphql.infrastructure.datamodel.book.BookSectionPageEntity;

public class BookSectionPageEntityConverter {
    public static BookSectionPageEntity toBookSectionPageEntity(Map<String, Object> result) {
        Map<Object, Object> baseResult = (Map<Object, Object>) result.get("contentBase");
        int number = (int) result.get("contentNumber");

        return BookSectionPageEntity.builder()
                                    .id(VertexConverter.toId(baseResult))
                                    .title(VertexConverter.toString(BookVertexProperty.TITLE.getString(),
                                                                         baseResult))
                                    .canPreview(VertexConverter.toBool(PageVertexProperty.CAN_PREVIEW.getString(), baseResult))
                                    .number(number)
                                    .build();
    }

    public static BookSectionPageEntity toCourseSectionContentEntityForGuest(Map<String, Object> result) {
        Map<Object, Object> baseResult = (Map<Object, Object>) result.get("pageBase");
        int number = (int) result.get("pageNumber");

        return BookSectionPageEntity.builder()
                                    .id(VertexConverter.toId(baseResult))
                                    .title(VertexConverter.toString(BookVertexProperty.TITLE.getString(),
                                                                         baseResult))
                                    .canPreview(VertexConverter.toBool(PageVertexProperty.CAN_PREVIEW.getString(), baseResult))
                                    .number(number)
                                    .build();
    }
}
