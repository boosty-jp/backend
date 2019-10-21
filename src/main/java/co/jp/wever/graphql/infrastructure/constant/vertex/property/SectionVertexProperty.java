package co.jp.wever.graphql.infrastructure.constant.vertex.property;

public enum SectionVertexProperty {
    TITLE("title"), TEXT("text"), CREATE_TIME("createTime"), UPDATE_TIME("updateTime");

    private String value;

    private SectionVertexProperty(String value) {
        this.value = value;
    }
}
