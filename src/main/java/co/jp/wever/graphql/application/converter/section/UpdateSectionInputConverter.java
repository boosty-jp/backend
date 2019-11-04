package co.jp.wever.graphql.application.converter.section;

import org.springframework.http.HttpStatus;

import java.util.Map;

import co.jp.wever.graphql.application.datamodel.request.UpdateSectionInput;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import graphql.schema.DataFetchingEnvironment;

public class UpdateSectionInputConverter {
    public static UpdateSectionInput toUpdateSectionInput(DataFetchingEnvironment request) {
        try {
            Map<String, Object> sectionInputMap = (Map) request.getArgument("section");
            return UpdateSectionInput.builder()
                                     .id(sectionInputMap.get("id").toString())
                                     .title(sectionInputMap.get("title").toString())
                                     .text(sectionInputMap.get("text").toString())
                                     .number((int) sectionInputMap.get("number"))
                                     .build();
        } catch (Exception e) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }

    public static UpdateSectionInput toUpdateSectionInput(Map<String, Object> request) {
        try {
            return UpdateSectionInput.builder()
                                     .id(request.get("id").toString())
                                     .title(request.get("title").toString())
                                     .text(request.get("text").toString())
                                     .number((int) request.get("number"))
                                     .build();
        } catch (Exception e) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }
}
