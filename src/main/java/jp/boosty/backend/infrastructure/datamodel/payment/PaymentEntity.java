package jp.boosty.backend.infrastructure.datamodel.payment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentEntity {
    private int amount;
    private String status;
    private String bookId;
    private String purchaseUserId;
}
