package jp.boosty.backend.infrastructure.converter.entity.user;

import jp.boosty.backend.infrastructure.constant.vertex.property.UserVertexProperty;
import jp.boosty.backend.infrastructure.converter.common.VertexConverter;
import jp.boosty.backend.infrastructure.datamodel.user.UserEntity;

import java.util.Map;

public class UserEntityConverter {
    public static UserEntity toUserEntity(Map<Object, Object> userVertex) {
        return UserEntity.builder()
                         .userId(VertexConverter.toId(userVertex))
                         .url(VertexConverter.toString("url", userVertex))
                         .imageUrl(VertexConverter.toString("imageUrl", userVertex))
                         .displayName(VertexConverter.toString(UserVertexProperty.DISPLAY_NAME.getString(), userVertex))
                         .description(VertexConverter.toString(UserVertexProperty.DESCRIPTION.getString(), userVertex))
                         .twitterId(VertexConverter.toString(UserVertexProperty.TWITTER_ID.getString(), userVertex))
                         .githubId(VertexConverter.toString(UserVertexProperty.GITHUB_ID.getString(), userVertex))
                         .stripeId(VertexConverter.toString(UserVertexProperty.STRIPE_ID.getString(), userVertex))
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
                         .githubId(VertexConverter.toString(UserVertexProperty.GITHUB_ID.getString(), userVertex))
                         .stripeId(VertexConverter.toString(UserVertexProperty.STRIPE_ID.getString(), userVertex))
                         .deleted(VertexConverter.toBool(UserVertexProperty.DELETED.getString(), userVertex))
                         .build();
    }
}
