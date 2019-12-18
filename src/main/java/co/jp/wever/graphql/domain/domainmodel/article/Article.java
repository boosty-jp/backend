package co.jp.wever.graphql.domain.domainmodel.article;

import java.util.List;

import co.jp.wever.graphql.application.datamodel.request.Requester;
import co.jp.wever.graphql.domain.domainmodel.article.action.ArticleUserAction;
import co.jp.wever.graphql.domain.domainmodel.article.base.ArticleBase;
import co.jp.wever.graphql.domain.domainmodel.article.base.ArticleStatus;
import co.jp.wever.graphql.domain.domainmodel.article.statistics.ArticleStatistics;
import co.jp.wever.graphql.domain.domainmodel.tag.Tag;
import co.jp.wever.graphql.domain.domainmodel.user.User;
import co.jp.wever.graphql.domain.domainmodel.user.UserId;

public class Article {
    private ArticleBase base;
    private List<Tag> tags;
    private User author;
    private ArticleStatistics statistics;
    private ArticleUserAction userAction;

    public Article(
        ArticleBase base, List<Tag> tags, User author, ArticleStatistics statistics, ArticleUserAction userAction) {
        this.base = base;
        this.tags = tags;
        this.author = author;
        this.statistics = statistics;
        this.userAction = userAction;
    }

    public boolean canRead(Requester requester) {
        if (base.getStatus().equals(ArticleStatus.PUBLISHED)) {
            return true;
        }

        if (requester.isGuest()) {
            return false;
        }

        return author.getUserId().same(UserId.of(requester.getUserId()));
    }

    public boolean canDelete(UserId userId) {
        if (!author.getUserId().same(userId)) {
            return false;
        }

        return !base.getStatus().getString().equals(ArticleStatus.DELETED.getString());
    }

    public boolean canUpdate(UserId userId) {
        if (!author.getUserId().same(userId)) {
            return false;
        }

        return !base.getStatus().getString().equals(ArticleStatus.DELETED.getString());
    }

    public boolean canPublish(UserId userId) {
        return author.getUserId().same(userId);

        //        if (base.getStatus().getString().equals(ArticleStatus.DELETED.getString())) {
        //            return false;
        //        }

        //        return !base.getStatus().getString().equals(ArticleStatus.PUBLISHED.getString());
    }

    public boolean canDraft(UserId userId) {
        if (!author.getUserId().same(userId)) {
            return false;
        }

        return !base.getStatus().getString().equals(ArticleStatus.DELETED.getString());
    }

    public ArticleBase getBase() {
        return base;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public User getAuthor() {
        return author;
    }

    public ArticleStatistics getStatistics() {
        return statistics;
    }

    public ArticleUserAction getUserAction() {
        return userAction;
    }
}
