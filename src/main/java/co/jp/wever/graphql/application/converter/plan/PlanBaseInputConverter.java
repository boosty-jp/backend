package co.jp.wever.graphql.application.converter.plan;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

import co.jp.wever.graphql.application.datamodel.request.PlanBaseInput;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import graphql.schema.DataFetchingEnvironment;

public class PlanBaseInputConverter {
    public static PlanBaseInput toPlanBaseInput(DataFetchingEnvironment request) {
        try {
            Map<String, Object> base = request.getArgument("base");

            return PlanBaseInput.builder()
                                .id(base.get("id").toString())
                                .title(base.get("title").toString())
                                .description(base.get("description").toString())
                                .imageUrl(base.get("imageUrl").toString())
                                .tags((List<String>) base.get("tags"))
                                .build();
        } catch (Exception e) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }
}
