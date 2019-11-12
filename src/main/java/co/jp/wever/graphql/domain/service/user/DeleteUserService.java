package co.jp.wever.graphql.domain.service.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import co.jp.wever.graphql.application.datamodel.request.Requester;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import co.jp.wever.graphql.infrastructure.repository.user.DeleteUserRepositoryImpl;

@Service
public class DeleteUserService {

    private final DeleteUserRepositoryImpl deleteUserRepository;

    public DeleteUserService(DeleteUserRepositoryImpl deleteUserRepository) {
        this.deleteUserRepository = deleteUserRepository;
    }

    public void deleteUser(Requester requester) {
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        deleteUserRepository.deleteUser(requester.getUserId());
    }
}
