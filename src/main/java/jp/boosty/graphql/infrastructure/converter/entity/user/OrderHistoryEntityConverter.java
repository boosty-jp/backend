package jp.boosty.graphql.infrastructure.converter.entity.user;

import jp.boosty.graphql.infrastructure.constant.vertex.property.BookVertexProperty;
import jp.boosty.graphql.infrastructure.constant.vertex.property.DateProperty;
import jp.boosty.graphql.infrastructure.converter.common.VertexConverter;
import jp.boosty.graphql.infrastructure.datamodel.user.OrderHistoryEntity;

import java.util.Map;

public class OrderHistoryEntityConverter {
    public static OrderHistoryEntity toOrderHistoryEntity(Map<String, Object> result) {
        Map<Object, Object> base = (Map<Object, Object>) result.get("base");
        Map<Object, Object> author = (Map<Object, Object>) result.get("author");
        Map<Object, Object> edge = (Map<Object, Object>) result.get("edge");

        return OrderHistoryEntity.builder()
                                 .bookId(VertexConverter.toId(base))
                                 .title(VertexConverter.toString(BookVertexProperty.TITLE.getString(), base))
                                 .imageUrl(VertexConverter.toString(BookVertexProperty.IMAGE_URL.getString(), base))
                                 .author(UserEntityConverter.toUserEntity(author))
                                 .price((int) edge.get(BookVertexProperty.PRICE.getString()))
                                 .purchaseDate((long) edge.get(DateProperty.CREATE_TIME.getString()))
                                 .build();
    }
}