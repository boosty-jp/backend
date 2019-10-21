package co.jp.wever.graphql.infrastructure.datamodel.article;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleStatisticsEntity {
    private long likeCount;
    private long learnedCount;
}
