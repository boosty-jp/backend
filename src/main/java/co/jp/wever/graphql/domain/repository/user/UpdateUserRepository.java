package co.jp.wever.graphql.domain.repository.user;

import org.springframework.stereotype.Repository;

@Repository
public interface UpdateUserRepository {
    void updateOne(String userId);
    void followUser(String targetUserId, String followerUserId);
    void unFollowUser(String targetUserId, String followerUserId);
}
