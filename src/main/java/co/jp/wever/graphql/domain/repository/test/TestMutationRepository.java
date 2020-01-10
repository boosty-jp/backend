package co.jp.wever.graphql.domain.repository.test;

import org.springframework.stereotype.Repository;

import co.jp.wever.graphql.domain.domainmodel.test.Test;

@Repository
public interface TestMutationRepository {
    void publish(String testId , Test test, String userId);

    String publishByEntry(Test test, String userId);

    void draft(String testId , Test test, String userId);

    String draftByEntry(Test test, String userId);

}
