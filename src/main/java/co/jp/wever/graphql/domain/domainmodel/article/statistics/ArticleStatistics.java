package co.jp.wever.graphql.domain.domainmodel.article.statistics;

public class ArticleStatistics {

    private ArticleLikeCount likeCount;
    private ArticleBookmarkedCount bookmarkedCount;
    private ArticleLearnedCount learnedCount;

    public ArticleStatistics(
        ArticleLikeCount likeCount, ArticleBookmarkedCount bookmarkedCount, ArticleLearnedCount learnedCount) {
        this.likeCount = likeCount;
        this.bookmarkedCount = bookmarkedCount;
        this.learnedCount = learnedCount;
    }
}
