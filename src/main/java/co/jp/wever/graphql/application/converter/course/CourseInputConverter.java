package co.jp.wever.graphql.application.converter.course;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.jp.wever.graphql.application.datamodel.request.course.CourseInput;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import graphql.schema.DataFetchingEnvironment;

public class CourseInputConverter {
    public static CourseInput toCourseInput(DataFetchingEnvironment request) {
        try {
            Map<String, Object> course = request.getArgument("course");

            List<Map<String, Object>> sections = (List<Map<String, Object>>) course.get("sections");
            return CourseInput.builder()
                              .id(course.get("id").toString())
                              .title(course.get("title").toString())
                              .imageUrl(course.get("imageUrl").toString())
                              .description(course.get("description").toString())
                              .tagIds((List<String>) course.get("tagIds"))
                              .sections(sections.stream()
                                                .map(s -> SectionInputConverter.toSectionInput(s))
                                                .collect(Collectors.toList()))
                              .build();
        } catch (Exception e) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }
}
