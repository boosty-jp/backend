package co.jp.wever.graphql.infrastructure.constant;


public enum Vertex {
    PLAN("plan"), ARTICLE("article"), SECTION("section"), USER("user"), TAG("tag");

    private String value;

    private Vertex(String value) {
        this.value = value;
    }

}
