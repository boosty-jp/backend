package jp.boosty.backend.application.converter.page;

import jp.boosty.backend.application.datamodel.request.page.PageInput;

import org.springframework.http.HttpStatus;

import java.util.Map;

import jp.boosty.backend.domain.GraphQLCustomException;
import jp.boosty.backend.infrastructure.constant.GraphQLErrorMessage;

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
