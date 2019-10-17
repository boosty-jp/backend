package co.jp.wever.graphql.infrastructure.constant.vertex.label;


public enum VertexType {
    PLAN("plan"), ARTICLE("article"), SECTION("section"), USER("user"), TAG("tag");

    private String value;

    private VertexType(String value) {
        this.value = value;
    }
}
