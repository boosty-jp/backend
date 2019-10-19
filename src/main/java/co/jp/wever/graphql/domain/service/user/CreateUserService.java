package co.jp.wever.graphql.domain.service.user;

import org.springframework.stereotype.Service;

import co.jp.wever.graphql.application.datamodel.request.UserInput;
import co.jp.wever.graphql.domain.converter.user.UserConverter;
import co.jp.wever.graphql.domain.domainmodel.user.User;
import co.jp.wever.graphql.infrastructure.converter.entity.user.UserEntityConverter;
import co.jp.wever.graphql.infrastructure.repository.user.CreateUserRepositoryImpl;

@Service
public class CreateUserService {

    private final CreateUserRepositoryImpl createUserRepository;

    public CreateUserService(CreateUserRepositoryImpl createUserRepository) {
        this.createUserRepository = createUserRepository;
    }

    public String createUser(UserInput userInput) {
        User user = UserConverter.toUser(userInput);
        return createUserRepository.createOne(UserEntityConverter.toUserEntity(user));
    }
}
