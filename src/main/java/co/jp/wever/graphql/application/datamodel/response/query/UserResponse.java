package co.jp.wever.graphql.application.datamodel.response.query;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private String userId;
    private String displayName;
    private String imageUrl;
}
