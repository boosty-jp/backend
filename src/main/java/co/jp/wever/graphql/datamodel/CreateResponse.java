package co.jp.wever.graphql.datamodel;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateResponse {
    private String id;
    private ErrorResponse error;
}
