package co.jp.wever.graphql.infrastructure.constant.vertex.label;


public enum VertexLabel {
    PLAN("plan"), ARTICLE("article"), USER("user"), TAG("tag"), SKILL("skill");

    private String value;

    private VertexLabel(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
