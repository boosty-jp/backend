package jp.boosty.backend.infrastructure.converter.entity.page;

import jp.boosty.backend.infrastructure.constant.vertex.property.DateProperty;
import jp.boosty.backend.infrastructure.constant.vertex.property.PageVertexProperty;
import jp.boosty.backend.infrastructure.converter.common.VertexConverter;
import jp.boosty.backend.infrastructure.datamodel.page.PageEntity;

import java.util.Map;

public class PageEntityConverter {

    public static PageEntity toPageEntityForGuest(Map<String, Object> result) {
        Map<Object, Object> base = (Map<Object, Object>) result.get("base");
        long likeCount = (long) result.get("likeCount");

        return PageEntity.builder()
                         .id(VertexConverter.toId(base))
                         .title(VertexConverter.toString(PageVertexProperty.TITLE.getString(), base))
                         .text(VertexConverter.toString(PageVertexProperty.TEXT.getString(), base))
                         .liked(false)
                         .likeCount(likeCount)
                         .canPreview(VertexConverter.toBool(PageVertexProperty.CAN_PREVIEW.getString(), base))
                         .createdDate(VertexConverter.toLong(DateProperty.CREATE_TIME.getString(), base))
                         .updatedDate(VertexConverter.toLong(DateProperty.UPDATE_TIME.getString(), base))
                         .build();
    }

    public static PageEntity toPageEntity(Map<String, Object> result) {
        Map<Object, Object> base = (Map<Object, Object>) result.get("base");
        boolean liked = (boolean) result.get("liked");
        long likeCount = (long) result.get("likeCount");

        return PageEntity.builder()
                         .id(VertexConverter.toId(base))
                         .title(VertexConverter.toString(PageVertexProperty.TITLE.getString(), base))
                         .text(VertexConverter.toString(PageVertexProperty.TEXT.getString(), base))
                         .liked(liked)
                         .likeCount(likeCount)
                         .canPreview(VertexConverter.toBool(PageVertexProperty.CAN_PREVIEW.getString(), base))
                         .createdDate(VertexConverter.toLong(DateProperty.CREATE_TIME.getString(), base))
                         .updatedDate(VertexConverter.toLong(DateProperty.UPDATE_TIME.getString(), base))
                         .build();
    }
}
