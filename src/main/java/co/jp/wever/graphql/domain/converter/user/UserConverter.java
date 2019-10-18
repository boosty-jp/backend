package co.jp.wever.graphql.domain.converter.user;

import co.jp.wever.graphql.domain.domainmodel.user.User;
import co.jp.wever.graphql.infrastructure.datamodel.user.UserEntity;

public class UserConverter {
    public static User toUser(UserEntity user) {
        return User.of(user.getUserId());
    }
}
