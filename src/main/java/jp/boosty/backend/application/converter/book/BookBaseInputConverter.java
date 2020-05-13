package jp.boosty.backend.application.converter.book;

import org.springframework.http.HttpStatus;

import java.util.Map;

import jp.boosty.backend.application.datamodel.request.book.BookBaseInput;
import jp.boosty.backend.domain.GraphQLCustomException;
import jp.boosty.backend.infrastructure.constant.GraphQLErrorMessage;
import graphql.schema.DataFetchingEnvironment;

public class BookBaseInputConverter {
    public static BookBaseInput toBookBaseInput(DataFetchingEnvironment request) {
        try {
            Map<String, Object> book = request.getArgument("bookBase");

            return BookBaseInput.builder()
                                .title(book.get("title").toString())
                                .description(book.get("description").toString())
                                .price((int) book.get("price"))
                                .build();
        } catch (Exception e) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }
}
