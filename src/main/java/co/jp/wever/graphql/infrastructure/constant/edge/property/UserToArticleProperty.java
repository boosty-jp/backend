package co.jp.wever.graphql.infrastructure.constant.edge.property;

public enum UserToArticleProperty {
    PUBLISHED_TIME("publishedTime"),
    DRAFTED_TIME("draftedTime"),
    LEARNED_TIME("learnedTime"),
    LIKED_TIME("likedTime");

    private String value;

    private UserToArticleProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
