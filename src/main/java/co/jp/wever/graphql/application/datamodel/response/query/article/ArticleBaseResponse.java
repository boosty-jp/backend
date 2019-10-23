package co.jp.wever.graphql.application.datamodel.response.query.article;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleBaseResponse {
    private String id;
    private String title;
    private String imageUrl;
    private String description;
    private String status;
    private String createDate;
    private String updateDate;
}
