package jp.boosty.backend.application.converter.user;

import jp.boosty.backend.application.datamodel.response.query.user.UserResponse;
import jp.boosty.backend.domain.domainmodel.user.User;
import jp.boosty.backend.infrastructure.datamodel.user.UserEntity;

public class UserResponseConverter {
    public static UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                           .id(user.getUserId().getValue())
                           .displayName(user.getDisplayName().getValue())
                           .description(user.getDescription().getValue())
                           .imageUrl(user.getImageUrl().getValue())
                           .url(user.getUrl().getValue())
                           .twitterId(user.getTwitterId().getValue())
                           .githubId(user.getGithubId().getValue())
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
                           .githubId(user.getGithubId())
                           .build();
    }
}
