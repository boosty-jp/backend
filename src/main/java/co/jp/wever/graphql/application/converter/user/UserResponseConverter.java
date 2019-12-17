package co.jp.wever.graphql.application.converter.user;

import co.jp.wever.graphql.application.datamodel.response.query.user.UserResponse;
import co.jp.wever.graphql.domain.domainmodel.user.User;
import co.jp.wever.graphql.infrastructure.datamodel.user.UserEntity;

public class UserResponseConverter {
    public static UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                           .id(user.getUserId().getValue())
                           .displayName(user.getDisplayName().getValue())
                           .description(user.getDescription().getValue())
                           .imageUrl(user.getImageUrl().getValue())
                           .url(user.getUrl().getValue())
                           .twitterId(user.getTwitterId().getValue())
                           .facebookId(user.getFacebookId().getValue())
                           .build();
    }

    public static UserResponse toUserResponse(UserEntity user) {
        return UserResponse.builder()
                           .id(user.getUserId())
                           .displayName(user.getDisplayName())
                           .description(user.getDescription())
                           .imageUrl(user.getImageUrl())
                           .url(user.getUrl())
                           .twitterId(user.getTwitterId())
                           .facebookId(user.getFacebookId())
                           .build();
    }
}
