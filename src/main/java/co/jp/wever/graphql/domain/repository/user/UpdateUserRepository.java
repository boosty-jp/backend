package co.jp.wever.graphql.domain.repository.user;

import org.springframework.stereotype.Repository;

import co.jp.wever.graphql.infrastructure.datamodel.user.UserEntity;

@Repository
public interface UpdateUserRepository {
    void updateOne(UserEntity userEntity);
    void followUser(String targetUserId, String followerUserId);
    void unFollowUser(String targetUserId, String followerUserId);
}
