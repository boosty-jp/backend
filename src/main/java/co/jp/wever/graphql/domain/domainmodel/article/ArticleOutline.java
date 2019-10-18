package co.jp.wever.graphql.domain.domainmodel.article;

import co.jp.wever.graphql.domain.domainmodel.article.base.ArticleBase;
import co.jp.wever.graphql.domain.domainmodel.article.base.ArticleStatus;
import co.jp.wever.graphql.domain.domainmodel.article.statistics.ArticleStatistics;
import co.jp.wever.graphql.domain.domainmodel.user.User;

public class ArticleOutline {
    private ArticleBase articleBase;
    private User author;
    private ArticleStatistics statistics;

    public ArticleOutline(
        ArticleBase articleBase, ArticleStatistics statistics, User user) {
        this.articleBase = articleBase;
        this.statistics = statistics;
        this.author = user;
    }

    public boolean canRead(User user) {
        if (!articleBase.getStatus().equals(ArticleStatus.PUBLISHED) && !author.isSame(user)) {
            return false;
        }
        return true;
    }
}
