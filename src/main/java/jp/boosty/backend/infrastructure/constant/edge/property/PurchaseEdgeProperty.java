package jp.boosty.backend.infrastructure.constant.edge.property;

public enum PurchaseEdgeProperty {
    PAYMENT_INTENT_ID("paymentIntentId"),
    PRICE("price");

    private String value;

    PurchaseEdgeProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
