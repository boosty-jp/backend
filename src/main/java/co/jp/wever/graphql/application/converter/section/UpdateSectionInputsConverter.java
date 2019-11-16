package co.jp.wever.graphql.application.converter.section;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.jp.wever.graphql.application.datamodel.request.UpdateSectionInput;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import graphql.schema.DataFetchingEnvironment;

public class UpdateSectionInputsConverter {
    public static List<UpdateSectionInput> toUpdateSectionInputs(DataFetchingEnvironment request) {
        try {
            List<Map<String, Object>> elementMaps = request.getArgument("sections");

            return elementMaps.stream()
                              .map(e -> UpdateSectionInputConverter.toUpdateSectionInput(e))
                              .collect(Collectors.toList());
        } catch (Exception e) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }
}
