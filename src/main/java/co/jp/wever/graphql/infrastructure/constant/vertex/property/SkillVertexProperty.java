package co.jp.wever.graphql.infrastructure.constant.vertex.property;

public enum SkillVertexProperty {
    NAME("name");

    private String value;

    private SkillVertexProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
