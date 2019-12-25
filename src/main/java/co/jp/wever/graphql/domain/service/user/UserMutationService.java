package co.jp.wever.graphql.domain.service.user;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import co.jp.wever.graphql.application.datamodel.request.user.Requester;
import co.jp.wever.graphql.application.datamodel.request.user.UserInput;
import co.jp.wever.graphql.application.datamodel.request.user.UserSettingInput;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.converter.user.UserConverter;
import co.jp.wever.graphql.domain.domainmodel.user.User;
import co.jp.wever.graphql.domain.domainmodel.user.UserDisplayName;
import co.jp.wever.graphql.domain.domainmodel.user.UserImageUrl;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import co.jp.wever.graphql.infrastructure.repository.user.UserMutationRepositoryImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserMutationService {
    private final UserMutationRepositoryImpl userMutationRepository;

    public UserMutationService(UserMutationRepositoryImpl userMutationRepository) {
        this.userMutationRepository = userMutationRepository;
    }

    public String createUser(String displayName, String imageUrl, Requester requester) {
        log.info("create user: {}", requester.getUserId());
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        UserDisplayName name = UserDisplayName.of(displayName);
        UserImageUrl url = UserImageUrl.of(imageUrl);
        return userMutationRepository.createOne(name.getValue(), url.getValue(), requester.getUserId());
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

        userMutationRepository.updateOne(user);
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

        try {
            FirebaseAuth.getInstance().deleteUser(requester.getUserId());
        } catch (FirebaseAuthException e) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }

        userMutationRepository.deleteUser(requester.getUserId());
    }
}
