package co.jp.wever.graphql.infrastructure.repository.user;

import org.springframework.stereotype.Component;

import co.jp.wever.graphql.domain.repository.user.CreateUserRepository;
import co.jp.wever.graphql.infrastructure.datamodel.user.UserEntity;

@Component
public class CreateUserRepositoryImpl implements CreateUserRepository {

    @Override
    public String createOne(UserEntity userEntity){
        return "";
    }
}
