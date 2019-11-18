package co.jp.wever.graphql.application.converter.section;

import org.springframework.http.HttpStatus;

import java.util.Map;

import co.jp.wever.graphql.application.datamodel.request.CreateSectionInput;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class CreateSectionInputConverter {
    public static CreateSectionInput toCreateSectionInput(Map<String, Object> input) {
        try {
            return CreateSectionInput.builder()
                               .title(input.get("title").toString())
                               .text(input.get("text").toString())
                               .number((int) input.get("number"))
                               .build();
        } catch (Exception e) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }
}