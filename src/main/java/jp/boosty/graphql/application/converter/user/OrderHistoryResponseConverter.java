package jp.boosty.graphql.application.converter.user;

import jp.boosty.graphql.infrastructure.util.DateToStringConverter;

import java.util.Date;

import jp.boosty.graphql.application.datamodel.response.query.user.OrderHistoryResponse;
import jp.boosty.graphql.infrastructure.datamodel.user.OrderHistoryEntity;

public class OrderHistoryResponseConverter {
    public static OrderHistoryResponse toOrderHistoryResponse(OrderHistoryEntity entity) {
        return OrderHistoryResponse.builder()
                                   .bookId(entity.getBookId())
                                   .title(entity.getTitle())
                                   .price(entity.getPrice())
                                   .imageUrl(entity.getImageUrl())
                                   .author(UserResponseConverter.toUserResponse(entity.getAuthor()))
                                   .purchaseDate(DateToStringConverter.toDateString(new Date(entity.getPurchaseDate())))
                                   .build();
    }
}
