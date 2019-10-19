package co.jp.wever.graphql.domain.repository.user;

import org.springframework.stereotype.Repository;

@Repository
public interface CreateUserRepository {
    String createOne(String userId);
}
