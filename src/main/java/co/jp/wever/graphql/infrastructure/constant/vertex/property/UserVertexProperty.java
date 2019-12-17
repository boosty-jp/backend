package co.jp.wever.graphql.infrastructure.constant.vertex.property;

public enum UserVertexProperty {
    DISPLAY_NAME("displayName"), IMAGE_URL("imageUrl"), DESCRIPTION("description"), URL("url"), TWITTER_ID("twitterId"), FACEBOOK_ID(
        "facebookId"), LEARN_PUBLIC("learnPublic"), LIKE_PUBLIC("likePublic"), SKILL_PUBLIC("skillPublic"), DELETED(
        "deleted"), CREATED_TIME("createdTime"), UPDATED_TIME("updatedTime");

    private String value;

    private UserVertexProperty(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
