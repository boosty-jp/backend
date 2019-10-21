package co.jp.wever.graphql.domain.domainmodel.article.statistics;

public class ArticleStatistics {

    private ArticleLikeCount likeCount;
    private ArticleLearnedCount learnedCount;

    public ArticleStatistics(
        ArticleLikeCount likeCount, ArticleLearnedCount learnedCount) {
        this.likeCount = likeCount;
        this.learnedCount = learnedCount;
    }

    public ArticleLikeCount getLikeCount() {
        return likeCount;
    }

    public ArticleLearnedCount getLearnedCount() {
        return learnedCount;
    }

}
