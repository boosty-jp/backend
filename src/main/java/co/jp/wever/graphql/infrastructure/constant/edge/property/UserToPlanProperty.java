package co.jp.wever.graphql.infrastructure.constant.edge.property;

public enum UserToPlanProperty {
    PUBLISHED_TIME("publishedTime"), DRAFTED_TIME("draftedTime"), DELETED_TIME("deletedTime"), LEARN_STARTED_TIME("learnStartedTime"), LEARN_FINISHED_TIME(
        "learnFinishedTime");

    private String value;

    private UserToPlanProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
