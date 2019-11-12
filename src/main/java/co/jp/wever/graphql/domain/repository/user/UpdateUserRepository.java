package co.jp.wever.graphql.domain.repository.user;

import org.springframework.stereotype.Repository;

import java.util.List;

import co.jp.wever.graphql.infrastructure.datamodel.user.UserEntity;

@Repository
public interface UpdateUserRepository {
    void updateOne(UserEntity userEntity);
    void updateImageUrl(String imageUrl, String userId);
    void updateTags(List<String> tagIds, String userId);
    void followUser(String targetUserId, String followerUserId);
    void unFollowUser(String targetUserId, String followerUserId);
}
