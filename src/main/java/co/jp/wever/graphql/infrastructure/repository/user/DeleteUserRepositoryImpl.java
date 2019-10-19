package co.jp.wever.graphql.infrastructure.repository.user;

import org.springframework.stereotype.Component;

import co.jp.wever.graphql.domain.repository.user.DeleteUserRepository;

@Component
public class DeleteUserRepositoryImpl implements DeleteUserRepository {
    @Override
    public void deleteUser(String userId) {

    }
}
