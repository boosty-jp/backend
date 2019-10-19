package co.jp.wever.graphql.domain.repository.user;

import org.springframework.stereotype.Repository;

@Repository
public interface DeleteUserRepository {
    void deleteUser(String userId);
}
