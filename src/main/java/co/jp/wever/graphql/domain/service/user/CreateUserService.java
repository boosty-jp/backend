package co.jp.wever.graphql.domain.service.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import co.jp.wever.graphql.application.datamodel.request.Requester;
import co.jp.wever.graphql.application.datamodel.request.UserInput;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.converter.user.UserConverter;
import co.jp.wever.graphql.domain.domainmodel.user.User;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import co.jp.wever.graphql.infrastructure.converter.entity.user.UserEntityConverter;
import co.jp.wever.graphql.infrastructure.repository.user.CreateUserRepositoryImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CreateUserService {

    private final CreateUserRepositoryImpl createUserRepository;

    public CreateUserService(CreateUserRepositoryImpl createUserRepository) {
        this.createUserRepository = createUserRepository;
    }

    public String createUser(UserInput userInput, Requester requester) {
        log.info("create user: {}", userInput);
        if(requester.isGuest()){
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        User user = UserConverter.toUser(userInput, requester.getUserId());
        return createUserRepository.createOne(UserEntityConverter.toUserEntity(user));
    }
}
