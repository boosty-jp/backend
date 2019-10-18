package co.jp.wever.graphql.domain.domainmodel.article.base;

import co.jp.wever.graphql.domain.domainmodel.user.User;

public class ArticleBase {
    private ArticleId id;
    private Articletitle title;
    private ArticleDescription description;
    private ArticleImageUrl imageUrl;
    private ArticleStatus status;
    private ArticleDate date;

    public ArticleBase(
        ArticleId id,
        Articletitle title,
        ArticleDescription description,
        ArticleImageUrl imageUrl,
        ArticleStatus status,
        ArticleDate date) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.status = status;
        this.date = date;
    }

    public ArticleStatus getStatus() {
        return status;
    }

    public String getId() {
        return id.getValue();
    }

    public String getTitle() {
        return title.getValue();
    }

    public String getDescription() {
        return description.getValue();
    }

    public String getImageUrl() {
        return imageUrl.getValue();
    }

    public ArticleDate getDate() {
        return date;
    }
}
