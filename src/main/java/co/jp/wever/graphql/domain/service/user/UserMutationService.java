package co.jp.wever.graphql.domain.service.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import co.jp.wever.graphql.application.datamodel.request.Requester;
import co.jp.wever.graphql.application.datamodel.request.UserInput;
import co.jp.wever.graphql.application.datamodel.request.UserSettingInput;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.converter.user.UserConverter;
import co.jp.wever.graphql.domain.domainmodel.user.User;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import co.jp.wever.graphql.infrastructure.converter.entity.user.UserEntityConverter;
import co.jp.wever.graphql.infrastructure.repository.user.UserMutationRepositoryImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserMutationService {
    private final UserMutationRepositoryImpl userMutationRepository;

    public UserMutationService(UserMutationRepositoryImpl userMutationRepository) {
        this.userMutationRepository = userMutationRepository;
    }

    public String createUser(UserInput userInput, Requester requester) {
        log.info("create user: {}", userInput);
        if(requester.isGuest()){
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        User user = UserConverter.toUser(userInput, requester.getUserId());
        return userMutationRepository.createOne(UserEntityConverter.toUserEntity(user));
    }

    public void updateUser(UserInput userInput, Requester requester) {
        log.info("update user name: {}", userInput.getDisplayName());
        log.info("update user imageUrl: {}", userInput.getImageUrl());
        log.info("update user url: {}", userInput.getUrl());
        log.info("update user description size: {}", userInput.getDescription().length());

        User user = UserConverter.toUser(userInput, requester.getUserId());

        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        userMutationRepository.updateOne(UserEntityConverter.toUserEntity(user));
    }

    public void updateUserSetting(UserSettingInput userSettingInput, Requester requester) {
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        userMutationRepository.updateSetting(requester.getUserId(), userSettingInput);
    }

    public void deleteUser(Requester requester) {
        log.info("delete user: {}", requester.getUserId());
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        userMutationRepository.deleteUser(requester.getUserId());
    }
}
