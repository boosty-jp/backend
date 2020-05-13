package jp.boosty.graphql.infrastructure.constant.payment;

public enum PaymentData {
    BOOK_ID("book_id"),
    PURCHASE_USER_ID("purchase_user_id");

    private String value;

    PaymentData(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
