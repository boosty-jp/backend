package jp.boosty.graphql.infrastructure.datamodel.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderHistoryEntity {
    private String bookId;
    private String title;
    private String imageUrl;
    private UserEntity author;
    private int price;
    private long purchaseDate;
}
