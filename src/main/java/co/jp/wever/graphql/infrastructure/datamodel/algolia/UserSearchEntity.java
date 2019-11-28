package co.jp.wever.graphql.infrastructure.datamodel.algolia;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSearchEntity {
    private String objectID;
    private String displayName;
    private String description;
    private String imageUrl;
    private List<String> tags;
}
