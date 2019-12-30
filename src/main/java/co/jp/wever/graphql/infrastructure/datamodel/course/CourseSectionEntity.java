package co.jp.wever.graphql.infrastructure.datamodel.course;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseSectionEntity {
    private String id;
    private int number;
    private String title;
    private List<CourseSectionContentEntity> courseSectionContents;
    private long createdDate;
    private long updatedDate;
}
