package co.jp.wever.graphql.application.user;

import org.springframework.stereotype.Service;

import co.jp.wever.graphql.application.datamodel.request.UserInput;
import co.jp.wever.graphql.domain.converter.user.UserConverter;
import co.jp.wever.graphql.domain.domainmodel.user.User;
import co.jp.wever.graphql.domain.domainmodel.user.UserId;
import co.jp.wever.graphql.infrastructure.converter.entity.user.UserEntityConverter;
import co.jp.wever.graphql.infrastructure.repository.user.UpdateUserRepositoryImpl;

@Service
public class UpdateUserService {

    private final UpdateUserRepositoryImpl updateUserRepository;

    public UpdateUserService(UpdateUserRepositoryImpl updateUserRepository) {
        this.updateUserRepository = updateUserRepository;
    }

    public void updateUser(UserInput userInput, String userId) {
        User user = UserConverter.toUser(userInput, userId);

        if (user.hasDuplicatedTagIds()) {
            throw new IllegalArgumentException();
        }

        this.updateUserRepository.updateOne(UserEntityConverter.toUserEntity(user));
    }

    public void followUser(String targetUserId, String followerUserId) {
        if (UserId.of(targetUserId).same(UserId.of(followerUserId))) {
            throw new IllegalArgumentException();
        }

        updateUserRepository.followUser(targetUserId, followerUserId);
    }

    public void unFollowUser(String targetUserId, String followerUserId) {
        if (UserId.of(targetUserId).same(UserId.of(followerUserId))) {
            throw new IllegalArgumentException();
        }


        updateUserRepository.unFollowUser(targetUserId, followerUserId);
    }
}
