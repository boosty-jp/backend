package co.jp.wever.graphql.application.converter.course;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.jp.wever.graphql.application.datamodel.request.ContentInput;
import co.jp.wever.graphql.application.datamodel.request.SectionInput;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class SectionInputConverter {
    public static SectionInput toSectionInput(Map<String, Object> section) {
        try {
            List<Map<String, Object>> contents = (List<Map<String, Object>>) section.get("contents");
            return SectionInput.builder()
                               .title(section.get("title").toString())
                               .number((int) section.get("number"))
                               .contents(contents.stream()
                                                 .map(c -> ContentInput.builder()
                                                                       .articleId(c.get("articleId").toString())
                                                                       .number(((int) c.get("number")))
                                                                       .build())
                                                 .collect(Collectors.toList()))
                               .build();
        } catch (Exception e) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }
}
