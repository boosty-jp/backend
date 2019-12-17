package co.jp.wever.graphql.infrastructure.converter.entity.user;

import java.util.Map;

import co.jp.wever.graphql.domain.domainmodel.user.User;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.UserVertexProperty;
import co.jp.wever.graphql.infrastructure.converter.common.VertexConverter;
import co.jp.wever.graphql.infrastructure.datamodel.user.UserEntity;

public class UserEntityConverter {
    public static UserEntity toUserEntity(User user) {
        return UserEntity.builder()
                         .userId(user.getUserId().getValue())
                         .displayName(user.getDisplayName().getValue())
                         .description(user.getDescription().getValue())
                         .imageUrl(user.getImageUrl().getValue())
                         .url(user.getUrl().getValue())
                         .build();
    }

    public static UserEntity toUserEntity(Map<Object, Object> userVertex) {
        return UserEntity.builder()
                         .userId(VertexConverter.toId(userVertex))
                         .url(VertexConverter.toString("url", userVertex))
                         .imageUrl(VertexConverter.toString("imageUrl", userVertex))
                         .displayName(VertexConverter.toString(UserVertexProperty.DISPLAY_NAME.getString(), userVertex))
                         .description(VertexConverter.toString(UserVertexProperty.DESCRIPTION.getString(), userVertex))
                         .twitterId(VertexConverter.toString(UserVertexProperty.TWITTER_ID.getString(), userVertex))
                         .facebookId(VertexConverter.toString(UserVertexProperty.FACEBOOK_ID.getString(), userVertex))
                         .build();
    }

    public static UserEntity toUserEntityFromVertex(Map<Object, Object> userVertex) {
        return UserEntity.builder()
                         .userId(VertexConverter.toId(userVertex))
                         .url(VertexConverter.toString(UserVertexProperty.URL.getString(), userVertex))
                         .imageUrl(VertexConverter.toString(UserVertexProperty.IMAGE_URL.getString(), userVertex))
                         .displayName(VertexConverter.toString(UserVertexProperty.DISPLAY_NAME.getString(), userVertex))
                         .description(VertexConverter.toString(UserVertexProperty.DESCRIPTION.getString(), userVertex))
                         .twitterId(VertexConverter.toString(UserVertexProperty.TWITTER_ID.getString(), userVertex))
                         .facebookId(VertexConverter.toString(UserVertexProperty.FACEBOOK_ID.getString(), userVertex))
                         .learnPublic(VertexConverter.toBool(UserVertexProperty.LEARN_PUBLIC.getString(), userVertex))
                         .likePublic(VertexConverter.toBool(UserVertexProperty.LIKE_PUBLIC.getString(), userVertex))
                         .skillPublic(VertexConverter.toBool(UserVertexProperty.SKILL_PUBLIC.getString(), userVertex))
                         .deleted(VertexConverter.toBool(UserVertexProperty.DELETED.getString(), userVertex))
                         .build();
    }

    public static UserEntity toUserEntityNoTag(Map<Object, Object> userVertex) {
        return UserEntity.builder()
                         .userId(VertexConverter.toId(userVertex))
                         .url(VertexConverter.toString(UserVertexProperty.URL.getString(), userVertex))
                         .imageUrl(VertexConverter.toString(UserVertexProperty.IMAGE_URL.getString(), userVertex))
                         .displayName(VertexConverter.toString(UserVertexProperty.DISPLAY_NAME.getString(), userVertex))
                         .description(VertexConverter.toString(UserVertexProperty.DESCRIPTION.getString(), userVertex))
                         .build();
    }
}
