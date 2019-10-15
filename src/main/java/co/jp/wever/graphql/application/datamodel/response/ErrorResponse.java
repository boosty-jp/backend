package co.jp.wever.graphql.application.datamodel.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
    private String errorCode;
    private String errorMessage;
}
