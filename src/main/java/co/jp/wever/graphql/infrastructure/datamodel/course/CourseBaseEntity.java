package co.jp.wever.graphql.infrastructure.datamodel.course;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseBaseEntity {
    private String id;
    private String title;
    private String imageUrl;
    private String description;
    private String status;
    private long createdDate;
    private long updatedDate;
}
