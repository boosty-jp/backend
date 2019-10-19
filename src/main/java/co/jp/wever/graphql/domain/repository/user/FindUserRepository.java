package co.jp.wever.graphql.domain.repository.user;

import org.springframework.stereotype.Repository;

import co.jp.wever.graphql.infrastructure.datamodel.user.UserEntity;

@Repository
public interface FindUserRepository {
    UserEntity findOne(String userId);
}
