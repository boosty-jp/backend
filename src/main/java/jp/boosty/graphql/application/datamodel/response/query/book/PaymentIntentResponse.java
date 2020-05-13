package jp.boosty.graphql.application.datamodel.response.query.book;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentIntentResponse {
    private int price;
    private String secret;
}
