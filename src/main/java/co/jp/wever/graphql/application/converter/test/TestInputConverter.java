package co.jp.wever.graphql.application.converter.test;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.jp.wever.graphql.application.datamodel.request.test.TestInput;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import graphql.schema.DataFetchingEnvironment;

public class TestInputConverter {
    public static TestInput toTestInput(DataFetchingEnvironment request) {
        try {
            Map<String, Object> test = request.getArgument("test");
            List<Map<String, Object>> questions = (List<Map<String, Object>>) test.get("questions");

            return TestInput.builder()
                            .id(test.get("id").toString())
                            .title(test.get("title").toString())
                            .description(test.get("description").toString())
                            .referenceCourseId(test.get("referenceCourseId").toString())
                            .questions(questions.stream()
                                                .map(q -> QuestionInputConverter.toQuestionInput(q))
                                                .collect(Collectors.toList()))
                            .build();
        } catch (Exception e) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }
}
