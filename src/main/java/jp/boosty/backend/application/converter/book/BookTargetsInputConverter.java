package jp.boosty.backend.application.converter.book;

import jp.boosty.backend.application.datamodel.request.book.BookTargetsInput;
import jp.boosty.backend.domain.GraphQLCustomException;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

import jp.boosty.backend.infrastructure.constant.GraphQLErrorMessage;

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
