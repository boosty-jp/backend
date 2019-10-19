package co.jp.wever.graphql.domain.converter.article;

import co.jp.wever.graphql.domain.domainmodel.article.statistics.ArticleBookmarkedCount;
import co.jp.wever.graphql.domain.domainmodel.article.statistics.ArticleLearnedCount;
import co.jp.wever.graphql.domain.domainmodel.article.statistics.ArticleLikeCount;
import co.jp.wever.graphql.domain.domainmodel.article.statistics.ArticleStatistics;
import co.jp.wever.graphql.infrastructure.datamodel.article.ArticleStatisticsEntity;

public class ArticleStatisticsConverter {
    public static ArticleStatistics toArticleStatistics(ArticleStatisticsEntity articleStatisticsEntity) {
        return new ArticleStatistics(ArticleLikeCount.of(articleStatisticsEntity.getLikeCount()),
                                     ArticleBookmarkedCount.of(articleStatisticsEntity.getBookmarkedCount()),
                                     ArticleLearnedCount.of(articleStatisticsEntity.getLearnedCount()));
    }
}
