package co.jp.wever.graphql.infrastructure.converter.entity.user;

import java.util.Map;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.domainmodel.user.User;
import co.jp.wever.graphql.infrastructure.converter.common.VertexConverter;
import co.jp.wever.graphql.infrastructure.converter.entity.tag.TagEntityConverter;
import co.jp.wever.graphql.infrastructure.datamodel.user.UserEntity;

public class UserEntityConverter {
    public static UserEntity toUserEntity(User user) {
        return UserEntity.builder()
                         .userId(user.getUserId().getValue())
                         .displayName(user.getDisplayName().getValue())
                         .description(user.getDescription().getValue())
                         .imageUrl(user.getImageUrl().getValue())
                         .url(user.getUrl().getValue())
                         .tags(user.getTags()
                                   .stream()
                                   .map(t -> TagEntityConverter.toTagEntity(t))
                                   .collect(Collectors.toList()))
                         .build();
    }

    public static UserEntity toUserEntity(Map<Object, Object> userVertex) {
        return UserEntity.builder()
                         .userId(VertexConverter.toId(userVertex))
                         .url(VertexConverter.toString("url", userVertex))
                         .imageUrl(VertexConverter.toString("imageUrl", userVertex))
                         .displayName(VertexConverter.toString("displayName", userVertex))
                         .build();
    }
}
