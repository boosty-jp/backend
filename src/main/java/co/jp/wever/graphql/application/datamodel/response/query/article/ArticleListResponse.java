package co.jp.wever.graphql.application.datamodel.response.query.article;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleListResponse {
    private List<ArticleResponse> articles;
    private long sumCount;
}
