package co.jp.wever.graphql.domain.repository.test;


import org.springframework.stereotype.Repository;

import co.jp.wever.graphql.infrastructure.datamodel.test.TestEntity;

@Repository
public interface TestQueryRepository {
    TestEntity findOne(String testId, String userId);
    TestEntity findOneForGuest(String testId);
    String findAuthorId(String articleId);
}
