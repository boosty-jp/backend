package co.jp.wever.graphql.application.converter.article;

import co.jp.wever.graphql.application.datamodel.response.query.article.ArticleStatisticsResponse;
import co.jp.wever.graphql.domain.domainmodel.article.statistics.ArticleStatistics;

public class ArticleStatisticsResponseConverter {
    public static ArticleStatisticsResponse toArticleStatisticsResponse(ArticleStatistics articleStatistics) {
        return ArticleStatisticsResponse.builder()
                                        .learned(articleStatistics.getLearnedCount().getValue())
                                        .like(articleStatistics.getLikeCount().getValue())
                                        .build();
    }
}
