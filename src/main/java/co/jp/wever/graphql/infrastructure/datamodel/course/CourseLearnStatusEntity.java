package co.jp.wever.graphql.infrastructure.datamodel.course;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseLearnStatusEntity {
    private int progress;
    private String status;
}
