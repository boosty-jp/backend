package co.jp.wever.graphql.infrastructure.datamodel.course;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseSectionContentEntity {
    private String id;
    private long number;
    private String title;
    private boolean learned;
}
