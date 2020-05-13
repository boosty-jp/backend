package jp.boosty.graphql.application.converter.book;

import org.springframework.http.HttpStatus;

import java.util.Map;

import jp.boosty.graphql.application.datamodel.request.book.BookBaseInput;
import jp.boosty.graphql.domain.GraphQLCustomException;
import jp.boosty.graphql.infrastructure.constant.GraphQLErrorMessage;
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
