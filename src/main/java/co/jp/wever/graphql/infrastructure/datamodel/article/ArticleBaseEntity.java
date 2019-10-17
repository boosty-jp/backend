package co.jp.wever.graphql.infrastructure.datamodel.article;

import java.util.Date;

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
    private Date createdDate;
    private Date updatedDate;
}