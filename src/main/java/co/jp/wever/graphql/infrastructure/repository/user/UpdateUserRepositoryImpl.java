package co.jp.wever.graphql.infrastructure.repository.user;

import org.springframework.stereotype.Component;

import co.jp.wever.graphql.domain.repository.user.UpdateUserRepository;
import co.jp.wever.graphql.infrastructure.datamodel.user.UserEntity;

@Component
public class UpdateUserRepositoryImpl implements UpdateUserRepository {

    public void updateOne(UserEntity userEntity) {

    }

    public void followUser(String targetUserId, String followerUserId) {

    }

    public void unFollowUser(String targetUserId, String followerUserId) {

    }
}
