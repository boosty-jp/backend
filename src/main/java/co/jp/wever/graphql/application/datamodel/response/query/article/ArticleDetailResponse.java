package co.jp.wever.graphql.application.datamodel.response.query.article;

import java.util.List;

import co.jp.wever.graphql.application.datamodel.response.query.TagResponse;
import co.jp.wever.graphql.application.datamodel.response.query.UserResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleDetailResponse {
    private ArticleBaseResponse base;
    private List<TagResponse> tags;
    private UserResponse author;
    private ArticleStatisticsResponse statistics;
    private ArticleUserActionResponse action;
}