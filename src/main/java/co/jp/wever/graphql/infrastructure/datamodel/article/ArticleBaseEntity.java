package co.jp.wever.graphql.infrastructure.datamodel.article;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleBaseEntity {
    private String id;
    private String title;
    private String description;
    private String imageUrl;
    private String status;
    private long createdDate;
    private long updatedDate;
}