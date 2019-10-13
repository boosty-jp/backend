package co.jp.wever.graphql.application.datamodel;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateResponse {
    private ErrorResponse error;
}
