package co.jp.wever.graphql.domain.domainmodel.user;

public class User {
    private UserId userId;

    private User(UserId userId) {
        this.userId = userId;
    }

    public static User of(String userId) {
        return new User(UserId.of(userId));
    }

    public String getUserId() {
        return userId.getValue();
    }

    public boolean isSame(User user) {
        return userId.same(user.getUserId());
    }
}
