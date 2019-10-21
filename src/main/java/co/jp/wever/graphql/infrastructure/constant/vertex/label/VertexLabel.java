package co.jp.wever.graphql.infrastructure.constant.vertex.label;


public enum VertexLabel {
    PLAN("plan"), ARTICLE("article"), SECTION("section"), USER("user"), TAG("tag");

    private String value;

    private VertexLabel(String value) {
        this.value = value;
    }
}
