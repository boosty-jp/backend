package jp.boosty.graphql.application.converter.page;

import jp.boosty.graphql.application.datamodel.request.page.PageInput;

import org.springframework.http.HttpStatus;

import java.util.Map;

import jp.boosty.graphql.domain.GraphQLCustomException;
import jp.boosty.graphql.infrastructure.constant.GraphQLErrorMessage;

import graphql.schema.DataFetchingEnvironment;

public class PageInputConverter {
    public static PageInput toPageInput(DataFetchingEnvironment request) {
        try {
            Map<String, Object> page = request.getArgument("page");

            return PageInput.builder()
                            .title(page.get("title").toString())
                            .text(page.get("text").toString())
                            .build();
        } catch (Exception e) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }
}
