package jp.boosty.graphql.domain.repository.user;

import jp.boosty.graphql.infrastructure.datamodel.user.UserEntity;

import org.springframework.stereotype.Repository;

import jp.boosty.graphql.infrastructure.datamodel.user.OrderHistoriesEntity;

@Repository
public interface UserQueryRepository {
    UserEntity findOne(String userId);

    OrderHistoriesEntity findOrderHistories(int page, String userId);
}
