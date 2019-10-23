package co.jp.wever.graphql.infrastructure.converter.entity.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.domainmodel.user.User;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.UserVertexProperty;
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

    public static UserEntity toUserEntity(Map<Object, Object> userVertex, List<Map<Object, Object>> userTagVertexs) {
        return UserEntity.builder()
                         .userId(VertexConverter.toId(userVertex))
                         .url(VertexConverter.toString("url", userVertex))
                         .imageUrl(VertexConverter.toString("imageUrl", userVertex))
                         .displayName(VertexConverter.toString(UserVertexProperty.DISPLAY_NAME.getString(), userVertex))
                         .description(VertexConverter.toString(UserVertexProperty.DESCRIPTION.getString(), userVertex))
                         .tags(userTagVertexs.stream()
                                             .map(t -> TagEntityConverter.toTagEntity(t))
                                             .collect(Collectors.toList()))
                         .build();
    }

    public static UserEntity toUserEntityByVertex(Map<String, Object> userVertex) {
        Map<Object, Object> userVertexMap = (Map<Object, Object>) userVertex.get("user");
        List<Map<Object, Object>> tagVertexMaps = (List<Map<Object, Object>>) userVertex.get("tags");
        return UserEntity.builder()
                         .userId(VertexConverter.toId(userVertexMap))
                         .url(VertexConverter.toString(UserVertexProperty.URL.getString(), userVertexMap))
                         .imageUrl(VertexConverter.toString(UserVertexProperty.IMAGE_URL.getString(), userVertexMap))
                         .displayName(VertexConverter.toString(UserVertexProperty.DISPLAY_NAME.getString(),
                                                               userVertexMap))
                         .description(VertexConverter.toString(UserVertexProperty.DESCRIPTION.getString(),
                                                               userVertexMap))
                         .tags(tagVertexMaps.stream()
                                            .map(t -> TagEntityConverter.toTagEntity(t))
                                            .collect(Collectors.toList()))
                         .build();
    }
}
