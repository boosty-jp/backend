package co.jp.wever.graphql.infrastructure.datamodel.user;

import java.util.List;

import co.jp.wever.graphql.infrastructure.datamodel.tag.TagEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserEntity {
    private String userId;
    private String displayName;
    private String imageUrl;
    private String description;
    private List<TagEntity> tags;
}
