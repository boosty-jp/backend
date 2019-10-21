package co.jp.wever.graphql.application.datamodel.response.query.article;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleStatisticsResponse {
    private long like;
    private long learned;
}
