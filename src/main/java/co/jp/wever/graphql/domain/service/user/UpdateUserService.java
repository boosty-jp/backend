package co.jp.wever.graphql.domain.service.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import co.jp.wever.graphql.application.datamodel.request.Requester;
import co.jp.wever.graphql.application.datamodel.request.UserInput;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.converter.user.UserConverter;
import co.jp.wever.graphql.domain.domainmodel.user.User;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import co.jp.wever.graphql.infrastructure.converter.entity.user.UserEntityConverter;
import co.jp.wever.graphql.infrastructure.repository.user.UpdateUserRepositoryImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UpdateUserService {

    private final UpdateUserRepositoryImpl updateUserRepository;

    public UpdateUserService(UpdateUserRepositoryImpl updateUserRepository) {
        this.updateUserRepository = updateUserRepository;
    }

    public void updateUser(UserInput userInput, Requester requester) {
        log.info("update user name: {}", userInput.getDisplayName());
        log.info("update user imageUrl: {}", userInput.getImageUrl());
        log.info("update user tags: {}", userInput.getTags());
        log.info("update user url: {}", userInput.getUrl());
        log.info("update user description size: {}", userInput.getDescription().length());

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

    public void updateUserImage(String imageUrl, Requester requester) {
        log.info("update userImage: {}", imageUrl);
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        this.updateUserRepository.updateImageUrl(imageUrl, requester.getUserId());
    }

    public void updateUserTags(List<String> tags, Requester requester) {
        log.info("update user tags: {}", tags);
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        if (tags.size() > 5) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_TAG_COUNT.getString());
        }
        this.updateUserRepository.updateTags(tags, requester.getUserId());
    }
}
