package co.jp.wever.graphql.application.datamodel.request;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInput {
    private String userId;
    private String displayName;
    private String description;
    private String imageUrl;
    private String url;
    private List<TagInput> tags;
}
