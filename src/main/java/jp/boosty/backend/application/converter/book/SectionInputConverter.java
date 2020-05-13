package jp.boosty.backend.application.converter.book;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jp.boosty.backend.application.datamodel.request.book.ContentInput;
import jp.boosty.backend.application.datamodel.request.book.SectionInput;
import jp.boosty.backend.domain.GraphQLCustomException;
import jp.boosty.backend.infrastructure.constant.GraphQLErrorMessage;

public class SectionInputConverter {
    public static SectionInput toSectionInput(Map<String, Object> section) {
        try {
            List<Map<String, Object>> contents = (List<Map<String, Object>>) section.get("articles");
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
