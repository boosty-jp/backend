package co.jp.wever.graphql.application.converter.course;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.jp.wever.graphql.application.datamodel.request.PlanElementInput;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import graphql.schema.DataFetchingEnvironment;

public class PlanElementInputsConverter {

    public static List<PlanElementInput> toPlanElementsInput(DataFetchingEnvironment request) {
        try {
            List<Map<String, Object>> elementMaps = request.getArgument("elements");

            return elementMaps.stream()
                              .map(e -> PlanElementInput.builder()
                                                        .id(e.get("id").toString())
                                                        .number((int) e.get("number"))
                                                        .build())
                              .collect(Collectors.toList());
        } catch (Exception e) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }
}
