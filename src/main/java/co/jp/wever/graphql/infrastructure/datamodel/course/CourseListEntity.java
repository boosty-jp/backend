package co.jp.wever.graphql.infrastructure.datamodel.course;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseListEntity {
    private List<CourseEntity> courses;
    private long sumCount;
}
