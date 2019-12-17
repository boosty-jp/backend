package co.jp.wever.graphql.infrastructure.datamodel.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserEntity {
    private String userId;
    private String displayName;
    private String imageUrl;
    private String description;
    private String url;
    private String twitterId;
    private String facebookId;
    private Boolean learnPublic;
    private Boolean likePublic;
    private Boolean skillPublic;
    private Boolean deleted;
}
