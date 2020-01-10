package co.jp.wever.graphql.infrastructure.converter.entity.test;

import java.util.Map;

import co.jp.wever.graphql.infrastructure.converter.entity.user.UserEntityConverter;
import co.jp.wever.graphql.infrastructure.datamodel.test.TestEntity;

public class TestEntityConverter {
    public static TestEntity toTestEntity(Map<String, Object> findResult) {
        Map<Object, Object> baseResult = (Map<Object, Object>) findResult.get("base");
        Map<Object, Object> questions = (Map<Object, Object>) findResult.get("questions");
        Map<Object, Object> authorResult = (Map<Object, Object>) findResult.get("author");
        String statusResult = findResult.get("status").toString();

        return TestEntity.builder()
                         .base(TestBaseEntityConverter.toTestBaseEntity(baseResult, statusResult))
                         .questions(QuestionsEntityConverter.toQuestionsEntity(questions))
                         .author(UserEntityConverter.toUserEntity(authorResult))
                         .build();
    }
}
