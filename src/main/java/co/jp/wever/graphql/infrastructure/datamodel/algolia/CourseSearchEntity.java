package co.jp.wever.graphql.infrastructure.datamodel.algolia;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseSearchEntity {
    private String objectID;
    private String title;
    private String description;
    private String imageUrl;
    private String authorId;
    private long articleCount;
    private long sectionCount;
    private long likeCount;
    private long learnedCount;
    private long viewCount;
    private long updateDate;
}
