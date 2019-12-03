package co.jp.wever.graphql.domain.service.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import co.jp.wever.graphql.application.datamodel.request.Requester;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.converter.user.UserConverter;
import co.jp.wever.graphql.domain.domainmodel.user.User;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import co.jp.wever.graphql.infrastructure.repository.user.FindUserRepositoryImpl;

@Service
public class FindUserService {
    private final FindUserRepositoryImpl findUserRepository;


    public FindUserService(FindUserRepositoryImpl findUserRepository) {
        this.findUserRepository = findUserRepository;
    }

    public User findUser(String userId) {
        if (userId.isEmpty()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.USER_ID_EMPTY.getString());
        }

        return UserConverter.toUser(findUserRepository.findOne(userId));
    }

    public User findProfile(Requester requester) {
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.UNAUTHORIZED.value(),
                                             GraphQLErrorMessage.NEED_LOGIN.getString());
        }
        return UserConverter.toUser(findUserRepository.findOne(requester.getUserId()));
    }
}
