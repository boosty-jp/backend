package jp.boosty.graphql.application.datamodel.request.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInput {
    private String displayName;
    private String description;
    private String imageUrl;
    private String url;
    private String twitterId;
    private String githubId;
}
