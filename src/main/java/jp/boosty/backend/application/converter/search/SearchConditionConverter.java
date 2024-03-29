package jp.boosty.backend.application.converter.search;

import org.springframework.http.HttpStatus;

import java.util.Map;

import jp.boosty.backend.application.datamodel.request.search.SearchConditionInput;
import jp.boosty.backend.domain.GraphQLCustomException;
import jp.boosty.backend.infrastructure.constant.GraphQLErrorMessage;
import graphql.schema.DataFetchingEnvironment;

public class SearchConditionConverter {
    public static SearchConditionInput toSearchCondition(DataFetchingEnvironment request) {
        try {
            Map<String, Object> condition = request.getArgument("condition");

            return SearchConditionInput.builder()
                                       .filter(condition.get("filter").toString())
                                       .sortField(condition.get("sortField").toString())
                                       .sortOrder(condition.get("sortOrder").toString())
                                       .page((int) condition.get("page"))
                                       .resultCount((int) condition.get("resultCount"))
                                       .build();
        } catch (Exception e) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }
}
