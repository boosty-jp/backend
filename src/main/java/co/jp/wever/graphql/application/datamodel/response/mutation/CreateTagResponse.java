package co.jp.wever.graphql.application.datamodel.response.mutation;

import co.jp.wever.graphql.application.datamodel.response.ErrorResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateTagResponse {
    private String id;
    private String name;
    private ErrorResponse error;
}
