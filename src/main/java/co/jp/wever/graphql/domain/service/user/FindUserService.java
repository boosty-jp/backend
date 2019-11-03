package co.jp.wever.graphql.domain.service.user;

import org.springframework.stereotype.Service;

import co.jp.wever.graphql.domain.converter.user.UserConverter;
import co.jp.wever.graphql.domain.domainmodel.user.User;
import co.jp.wever.graphql.infrastructure.repository.user.FindUserRepositoryImpl;

@Service
public class FindUserService {
    private final FindUserRepositoryImpl findUserRepository;


    public FindUserService(FindUserRepositoryImpl findUserRepository) {
        this.findUserRepository = findUserRepository;
    }

    public User findUser(String userId) {

        return UserConverter.toUser(findUserRepository.findOne(userId));
    }
}
