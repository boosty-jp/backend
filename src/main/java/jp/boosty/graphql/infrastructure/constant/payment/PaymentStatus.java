package jp.boosty.graphql.infrastructure.constant.payment;

public enum PaymentStatus {
    SUCCEEDED("succeeded");

    private String value;

    PaymentStatus(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
