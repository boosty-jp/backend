package co.jp.wever.graphql.application.converter.plan;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

import co.jp.wever.graphql.application.datamodel.request.PlanBaseInput;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class PlanBaseInputConverter {
    public static PlanBaseInput toPlanBaseInput(Map<String, Object> input) {
        try {
            return PlanBaseInput.builder()
                                .title(input.get("title").toString())
                                .description(input.get("description").toString())
                                .imageUrl(input.get("imageUrl").toString())
                                .tags((List<String>) input.get("tags"))
                                .build();
        } catch (Exception e) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }
}
