package jp.boosty.graphql.infrastructure.constant.vertex.property;

public enum UserVertexProperty {
    DISPLAY_NAME("displayName"),
    IMAGE_URL("imageUrl"),
    DESCRIPTION("description"),
    URL("url"),
    TWITTER_ID("twitterId"),
    GITHUB_ID("githubId"),
    STRIPE_ID("stripeId"),
    DELETED("deleted"),
    CREATED_TIME("createdTime"),
    UPDATED_TIME("updatedTime");

    private String value;

    private UserVertexProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
