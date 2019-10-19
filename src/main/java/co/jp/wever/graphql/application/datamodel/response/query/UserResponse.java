package co.jp.wever.graphql.application.datamodel.response.query;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private String userId;
    private String displayName;
    private String description;
    private String imageUrl;
    private String url;
    private List<TagResponse> tags;
}
