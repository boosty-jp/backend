package jp.boosty.graphql.domain.service.user;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import com.stripe.exception.StripeException;

import jp.boosty.graphql.application.datamodel.request.user.Requester;
import jp.boosty.graphql.domain.converter.user.UserConverter;
import jp.boosty.graphql.application.datamodel.request.user.UserInput;
import jp.boosty.graphql.domain.GraphQLCustomException;
import jp.boosty.graphql.domain.domainmodel.user.User;
import jp.boosty.graphql.domain.domainmodel.user.UserDisplayName;
import jp.boosty.graphql.domain.domainmodel.user.UserImageUrl;
import jp.boosty.graphql.infrastructure.constant.GraphQLErrorMessage;
import jp.boosty.graphql.infrastructure.repository.user.UserMutationRepositoryImpl;
import jp.boosty.graphql.infrastructure.repository.user.UserQueryRepositoryImpl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserMutationService {
    private final UserMutationRepositoryImpl userMutationRepository;
    private final UserQueryRepositoryImpl userQueryRepository;

    public UserMutationService(
        UserMutationRepositoryImpl userMutationRepository, UserQueryRepositoryImpl userQueryRepository) {
        this.userMutationRepository = userMutationRepository;
        this.userQueryRepository = userQueryRepository;
    }

    public String createUser(String displayName, String imageUrl, Requester requester) {
        log.info("create user: {} {} {}", requester.getUserId(), imageUrl, displayName);
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

    public void deleteUser(Requester requester) {
        log.info("delete user: {}", requester.getUserId());
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        try {
            FirebaseAuth.getInstance().deleteUser(requester.getUserId());
        } catch (FirebaseAuthException e) {
            log.info("firebase error: {}", e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }

        userMutationRepository.deleteUser(requester.getUserId());
    }

    public void registerStripe(String userId, String code, Requester requester) {
        log.info("register stripe: {} {} {}", userId, code, requester);
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        if (!userId.equals(requester.getUserId())) {
            // CSRF対策でStripeのリダイレクトURIからユーザーIDをもらうようにしている
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        String stripeId = userQueryRepository.findOne(userId).getStripeId();
        if (!StringUtil.isNullOrEmpty(stripeId)) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.ALREADY_STRIPE_REGISTERED.getString());
        }

        try {
            userMutationRepository.registerStripe(userId, code);
        } catch (StripeException e) {
            log.error(e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }
}
