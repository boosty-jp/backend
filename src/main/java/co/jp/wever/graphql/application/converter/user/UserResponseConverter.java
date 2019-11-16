package co.jp.wever.graphql.application.converter.user;

import java.util.stream.Collectors;

import co.jp.wever.graphql.application.converter.tag.TagResponseConverter;
import co.jp.wever.graphql.application.datamodel.response.query.UserResponse;
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
                           .tags(user.getTags()
                                     .stream()
                                     .map(t -> TagResponseConverter.toTagResponse(t))
                                     .collect(Collectors.toList()))
                           .build();
    }

    public static UserResponse toUserResponse(UserEntity user) {
        return UserResponse.builder()
                           .id(user.getUserId())
                           .displayName(user.getDisplayName())
                           .imageUrl(user.getImageUrl())
                           .build();
    }
}
