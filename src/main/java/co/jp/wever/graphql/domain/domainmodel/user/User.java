package co.jp.wever.graphql.domain.domainmodel.user;

public class User {

    private UserId userId;
    private UserDisplayName displayName;
    private UserDescription description;
    private UserImageUrl imageUrl;
    private UserUrl url;
    private UserTwitterId twitterId;
    private UserFacebookId facebookId;

    // TODO: privateにする
    public User(
        UserId userId,
        UserDisplayName displayName,
        UserDescription description,
        UserImageUrl imageUrl,
        UserUrl url,
        UserTwitterId twitterId,
        UserFacebookId facebookId) {
        this.userId = userId;
        this.description = description;
        this.displayName = displayName;
        this.imageUrl = imageUrl;
        this.url = url;
        this.twitterId = twitterId;
        this.facebookId = facebookId;
    }

    public static User of(
        String userId,
        String displayName,
        String description,
        String imageUrl,
        String url,
        String twitterId,
        String facebookId) {
        return new User(UserId.of(userId),
                        UserDisplayName.of(displayName),
                        UserDescription.of(description),
                        UserImageUrl.of(imageUrl),
                        UserUrl.of(url),
                        UserTwitterId.of(twitterId),
                        UserFacebookId.of(facebookId));
    }

    public UserId getUserId() {
        return userId;
    }

    public UserDisplayName getDisplayName() {
        return displayName;
    }

    public UserDescription getDescription() {
        return description;
    }

    public UserImageUrl getImageUrl() {
        return imageUrl;
    }

    public UserUrl getUrl() {
        return url;
    }

    public UserTwitterId getTwitterId() {
        return twitterId;
    }

    public UserFacebookId getFacebookId() {
        return facebookId;
    }
}
