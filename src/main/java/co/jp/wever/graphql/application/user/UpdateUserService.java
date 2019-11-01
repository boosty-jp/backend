package co.jp.wever.graphql.application.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import co.jp.wever.graphql.application.datamodel.request.UserInput;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.converter.user.UserConverter;
import co.jp.wever.graphql.domain.domainmodel.user.User;
import co.jp.wever.graphql.domain.domainmodel.user.UserId;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
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

        // ドメインに移動させる
        if(user.getTags().size() > 5) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_TAG_COUNT.getString());
        }

        if (user.hasDuplicatedTagIds()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.TAG_DUPLICATED.getString());
        }

        this.updateUserRepository.updateOne(UserEntityConverter.toUserEntity(user));
    }

    public void updateUserImage(String imageUrl, String userId) {
        this.updateUserRepository.updateImageUrl(imageUrl, userId);
    }

    public void followUser(String targetUserId, String followerUserId) {
        if (UserId.of(targetUserId).same(UserId.of(followerUserId))) {

            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.FOLLOW_OWN.getString());
        }

        updateUserRepository.followUser(targetUserId, followerUserId);
    }

    public void unFollowUser(String targetUserId, String followerUserId) {
        if (UserId.of(targetUserId).same(UserId.of(followerUserId))) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.FOLLOW_OWN.getString());
        }

        updateUserRepository.unFollowUser(targetUserId, followerUserId);
    }
}
