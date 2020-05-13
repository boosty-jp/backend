package jp.boosty.graphql.infrastructure.constant.edge.property;

public enum ViewEdgeProperty {
    PAGE_ID("pageId");

    private String value;

    ViewEdgeProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
