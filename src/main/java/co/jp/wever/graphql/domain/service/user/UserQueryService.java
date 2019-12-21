package co.jp.wever.graphql.domain.service.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import co.jp.wever.graphql.application.datamodel.request.user.Requester;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import co.jp.wever.graphql.infrastructure.datamodel.user.UserEntity;
import co.jp.wever.graphql.infrastructure.repository.user.UserQueryRepositoryImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserQueryService {
    private final UserQueryRepositoryImpl userQueryRepository;

    public UserQueryService(UserQueryRepositoryImpl userQueryRepository) {
        this.userQueryRepository = userQueryRepository;
    }

    public UserEntity findUser(String userId) {
        if (userId.isEmpty()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.USER_ID_EMPTY.getString());
        }

        UserEntity userEntity = userQueryRepository.findOne(userId);
        if(userEntity.getDeleted()){
            throw new GraphQLCustomException(HttpStatus.NOT_FOUND.value(),
                                             GraphQLErrorMessage.USER_NOT_FOUND.getString());
        }
        return userEntity;
    }

    public UserEntity findAccount(Requester requester) {
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.UNAUTHORIZED.value(),
                                             GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        UserEntity userEntity = userQueryRepository.findOne(requester.getUserId());
        if(userEntity.getDeleted()){
            throw new GraphQLCustomException(HttpStatus.NOT_FOUND.value(),
                                             GraphQLErrorMessage.USER_NOT_FOUND.getString());
        }
        return userEntity;
    }
}
