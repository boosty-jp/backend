package co.jp.wever.graphql.infrastructure.repository.user;

import org.springframework.stereotype.Component;

import co.jp.wever.graphql.domain.repository.user.UpdateUserRepository;

@Component
public class UpdateUserRepositoryImpl implements UpdateUserRepository {

    public void updateOne(String userId) {

    }

    public void followUser(String targetUserId, String followerUserId) {

    }

    public void unFollowUser(String targetUserId, String followerUserId) {

    }
}
