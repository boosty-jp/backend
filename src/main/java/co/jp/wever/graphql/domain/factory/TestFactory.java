package co.jp.wever.graphql.domain.factory;

import java.util.stream.Collectors;

import co.jp.wever.graphql.application.datamodel.request.test.TestInput;
import co.jp.wever.graphql.domain.domainmodel.content.ContentDescription;
import co.jp.wever.graphql.domain.domainmodel.content.ContentTitle;
import co.jp.wever.graphql.domain.domainmodel.test.Questions;
import co.jp.wever.graphql.domain.domainmodel.test.Test;

public class TestFactory {
    public static Test make(TestInput testInput) {
        ContentTitle title = ContentTitle.of(testInput.getTitle());
        ContentDescription description = ContentDescription.of(testInput.getDescription());
        Questions questions = Questions.of(testInput.getQuestions().stream().map(q -> QuestionFactory.make(q)).collect(Collectors.toList()));

        return Test.of(title, description, questions);
    }
}
