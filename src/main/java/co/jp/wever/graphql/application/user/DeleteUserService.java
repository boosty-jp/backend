package co.jp.wever.graphql.application.user;

import org.springframework.stereotype.Service;

import co.jp.wever.graphql.infrastructure.repository.user.DeleteUserRepositoryImpl;

@Service
public class DeleteUserService {

    private final DeleteUserRepositoryImpl deleteUserRepository;

    public DeleteUserService(DeleteUserRepositoryImpl deleteUserRepository) {
        this.deleteUserRepository = deleteUserRepository;
    }

    public void deleteUser(String userId){
        deleteUserRepository.deleteUser(userId);
    }
}
