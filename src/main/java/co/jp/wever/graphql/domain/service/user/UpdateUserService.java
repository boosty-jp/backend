package co.jp.wever.graphql.domain.service.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import co.jp.wever.graphql.application.datamodel.request.Requester;
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

    public void updateUser(UserInput userInput, Requester requester) {
        User user = UserConverter.toUser(userInput, requester.getUserId());

        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        // ドメインに移動させる
        if (user.getTags().size() > 5) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_TAG_COUNT.getString());
        }

        if (user.hasDuplicatedTagIds()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.TAG_DUPLICATED.getString());
        }

        if (!user.hasDisplayName()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.USER_NAME_EMPTY.getString());
        }

        this.updateUserRepository.updateOne(UserEntityConverter.toUserEntity(user));
    }

    public void updateUserImage(String imageUrl, String userId) {
        this.updateUserRepository.updateImageUrl(imageUrl, userId);
    }

    public void updateUserTags(List<String> tags, Requester requester) {
        if (tags.size() > 5) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_TAG_COUNT.getString());
        }
        this.updateUserRepository.updateTags(tags, requester.getUserId());
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
