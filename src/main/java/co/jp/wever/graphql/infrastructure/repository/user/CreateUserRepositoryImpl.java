package co.jp.wever.graphql.infrastructure.repository.user;

import org.springframework.stereotype.Component;

import co.jp.wever.graphql.domain.repository.user.CreateUserRepository;

@Component
public class CreateUserRepositoryImpl implements CreateUserRepository {

    @Override
    public String createOne(String userId){
        return "";
    }
}
