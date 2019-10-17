package co.jp.wever.graphql.infrastructure.datamodel.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserEntity {
    private String userId;
    private String displayName;
    private String imageUrl;
}
