package jp.boosty.graphql.application.converter.book;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jp.boosty.graphql.application.datamodel.request.book.BookInput;
import jp.boosty.graphql.domain.GraphQLCustomException;
import jp.boosty.graphql.infrastructure.constant.GraphQLErrorMessage;
import graphql.schema.DataFetchingEnvironment;

public class BookInputConverter {
    public static BookInput toBookInput(DataFetchingEnvironment request) {
        try {
            Map<String, Object> book = request.getArgument("book");

            List<Map<String, Object>> sections = (List<Map<String, Object>>) book.get("sections");
            return BookInput.builder()
                            .id(book.get("id").toString())
                            .title(book.get("title").toString())
                            .imageUrl(book.get("imageUrl").toString())
                            .description(book.get("description").toString())
                            .tagIds((List<String>) book.get("tagIds"))
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
