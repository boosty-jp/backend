package co.jp.wever.graphql.application.datamodel.response.query.article;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleBlockResponse {
    private String id;
    private String type;
    private String data;
}
