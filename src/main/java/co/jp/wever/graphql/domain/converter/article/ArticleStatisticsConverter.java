package co.jp.wever.graphql.domain.converter.article;

import co.jp.wever.graphql.domain.domainmodel.action.LearnedCount;
import co.jp.wever.graphql.domain.domainmodel.action.LikeCount;

public class ArticleStatisticsConverter {
    public static ArticleStatistics toArticleStatistics(ArticleStatisticsEntity articleStatisticsEntity) {
        return new ArticleStatistics(LikeCount.of(articleStatisticsEntity.getLikeCount()),
                                     LearnedCount.of(articleStatisticsEntity.getLearnedCount()));
    }
}
