package co.jp.wever.graphql.domain.domainmodel.article;

import java.util.List;

import co.jp.wever.graphql.domain.domainmodel.article.action.ArticleUserAction;
import co.jp.wever.graphql.domain.domainmodel.article.base.ArticleBase;
import co.jp.wever.graphql.domain.domainmodel.article.statistics.ArticleStatistics;
import co.jp.wever.graphql.domain.domainmodel.tag.Tag;
import co.jp.wever.graphql.domain.domainmodel.user.User;

public class ArticleDetail {

    private ArticleBase base;
    private List<Tag> tags;
    private User author;
    private ArticleStatistics statistics;
    private ArticleUserAction userAction;

    public ArticleDetail(
        ArticleBase base, List<Tag> tags, User author, ArticleStatistics statistics, ArticleUserAction userAction) {
        this.base = base;
        this.tags = tags;
        this.author = author;
        this.statistics = statistics;
        this.userAction = userAction;
    }
}
