package jp.boosty.backend.application.converter.user;

import jp.boosty.backend.infrastructure.util.DateToStringConverter;

import java.util.Date;

import jp.boosty.backend.application.datamodel.response.query.user.OrderHistoryResponse;
import jp.boosty.backend.infrastructure.datamodel.user.OrderHistoryEntity;

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
