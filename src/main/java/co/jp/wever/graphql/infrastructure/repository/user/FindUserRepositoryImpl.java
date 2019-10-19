package co.jp.wever.graphql.infrastructure.repository.user;

import org.springframework.stereotype.Component;

import co.jp.wever.graphql.domain.repository.user.FindUserRepository;
import co.jp.wever.graphql.infrastructure.datamodel.user.UserEntity;

@Component
public class FindUserRepositoryImpl implements FindUserRepository {
    @Override
    public UserEntity findOne(String userId) {
        return null;
    }
}
