package co.jp.wever.graphql.application.datamodel.response.query.course;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseListResponse {
    private List<CourseResponse> courses;
    private long sumCount;
}
