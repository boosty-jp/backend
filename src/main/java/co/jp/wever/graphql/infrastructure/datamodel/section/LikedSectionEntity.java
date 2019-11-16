package co.jp.wever.graphql.infrastructure.datamodel.section;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LikedSectionEntity {
    private String articleId;
    private SectionEntity sectionEntity;
}
