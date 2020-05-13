package jp.boosty.backend.infrastructure.converter.entity.page;

import java.util.Map;

import jp.boosty.backend.infrastructure.constant.vertex.property.BookVertexProperty;
import jp.boosty.backend.infrastructure.constant.vertex.property.PageVertexProperty;
import jp.boosty.backend.infrastructure.converter.common.VertexConverter;
import jp.boosty.backend.infrastructure.datamodel.page.LikedPageEntity;

public class LikedPageEntityConverter {
    public static LikedPageEntity toLikedPageEntity(Map<String, Object> result) {
        Map<Object, Object> base = (Map<Object, Object>) result.get("base");
        Map<Object, Object> book = (Map<Object, Object>) result.get("book");

        return LikedPageEntity.builder()
                              .id(VertexConverter.toId(base))
                              .title(VertexConverter.toString(PageVertexProperty.TITLE.getString(), base))
                              .bookId(VertexConverter.toId(book))
                              .bookTitle(VertexConverter.toString(BookVertexProperty.TITLE.getString(), book))
                              .bookImage(VertexConverter.toString(BookVertexProperty.IMAGE_URL.getString(), book))
                              .build();
    }
}
