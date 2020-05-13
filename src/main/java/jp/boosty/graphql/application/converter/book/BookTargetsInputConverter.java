package jp.boosty.graphql.application.converter.book;

import jp.boosty.graphql.application.datamodel.request.book.BookTargetsInput;
import jp.boosty.graphql.domain.GraphQLCustomException;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

import jp.boosty.graphql.infrastructure.constant.GraphQLErrorMessage;

import graphql.schema.DataFetchingEnvironment;

public class BookTargetsInputConverter {
    public static BookTargetsInput toBookTargetsInput(DataFetchingEnvironment request) {
        try {
            Map<String, Object> targets = request.getArgument("targets");

            return BookTargetsInput.builder()
                                   .levelStart((int) targets.get("levelStart"))
                                   .levelEnd((int) targets.get("levelEnd"))
                                   .targetsDescription((List<String>) targets.get("targetsDescriptions"))
                                   .build();
        } catch (Exception e) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }
}
