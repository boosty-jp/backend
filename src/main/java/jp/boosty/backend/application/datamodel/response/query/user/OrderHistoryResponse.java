package jp.boosty.backend.application.datamodel.response.query.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderHistoryResponse {
    private String bookId;
    private String title;
    private String imageUrl;
    private UserResponse author;
    private int price;
    private String purchaseDate;
}
