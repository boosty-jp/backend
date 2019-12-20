package co.jp.wever.graphql.infrastructure.datamodel.section;

import co.jp.wever.graphql.infrastructure.datamodel.course.CourseSectionEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LikedSectionEntity {
    private String articleId;
    private CourseSectionEntity courseSectionEntity;
}
