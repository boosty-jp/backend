package co.jp.wever.graphql.domain.domainmodel.article;

import java.util.List;

import co.jp.wever.graphql.domain.domainmodel.article.action.ArticleUserAction;
import co.jp.wever.graphql.domain.domainmodel.article.base.ArticleBase;
import co.jp.wever.graphql.domain.domainmodel.article.base.ArticleStatus;
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

    public boolean canRead(User user) {
        return base.getStatus().equals(ArticleStatus.PUBLISHED) || author.isSame(user);
    }

    public boolean canDelete(User user) {
        if (!author.isSame(user)) {
            return false;
        }

        return !base.getStatus().name().equals(ArticleStatus.DELETED.name());
    }

    public boolean canUpdate(User user) {
        if (!author.isSame(user)) {
            return false;
        }

        return !base.getStatus().name().equals(ArticleStatus.DELETED.name());
    }

    public boolean canPublish(User user) {
        if (!author.isSame(user)) {
            return false;
        }

        if (base.getStatus().name().equals(ArticleStatus.DELETED.name())) {
            return false;
        }

        return !base.getStatus().name().equals(ArticleStatus.PUBLISHED.name());
    }

    public boolean canDraft(User user) {
        if (!author.isSame(user)) {
            return false;
        }

        if (base.getStatus().name().equals(ArticleStatus.DELETED.name())) {
            return false;
        }

        return !base.getStatus().name().equals(ArticleStatus.DRAFTED.name());
    }
}
