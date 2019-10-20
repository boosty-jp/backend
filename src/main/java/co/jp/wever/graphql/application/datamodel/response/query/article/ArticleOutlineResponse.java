package co.jp.wever.graphql.application.datamodel.response.query.article;

import co.jp.wever.graphql.application.datamodel.response.query.UserResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleOutlineResponse {
    private ArticleBaseResponse base;
    private UserResponse author;
    private ArticleStatisticsResponse statistics;
}
