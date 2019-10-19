package co.jp.wever.graphql.application.converter.user;

import java.util.stream.Collectors;

import co.jp.wever.graphql.application.converter.tag.TagResponseConverter;
import co.jp.wever.graphql.application.datamodel.response.query.UserResponse;
import co.jp.wever.graphql.domain.domainmodel.user.User;

public class UserResponseConverter {
    public static UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                           .userId(user.getUserId().getValue())
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
}
