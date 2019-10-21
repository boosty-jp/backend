package co.jp.wever.graphql.infrastructure.constant.edge.property;

public enum UserToPlanProperty {
    PUBLISHED("published"),
    DRAFTED("drafted"),
    DELETED("deleted"),
    LEARN_STARTED("learnStarted"),
    LEARN_FINISHED("learnFinished");

    private String value;

    private UserToPlanProperty(String value) {
        this.value = value;
    }
}
