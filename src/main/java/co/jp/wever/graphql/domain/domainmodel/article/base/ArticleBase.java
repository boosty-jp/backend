package co.jp.wever.graphql.domain.domainmodel.article.base;

public class ArticleBase {
    private ArticleId id;
    private ArticleTitle title;
    private ArticleImageUrl imageUrl;
    private ArticleTextUrl textUrl;
    private ArticleStatus status;
    private ArticleDate date;

    public ArticleBase(
        ArticleId id,
        ArticleTitle title,
        ArticleImageUrl imageUrl,
        ArticleTextUrl textUrl,
        ArticleStatus status,
        ArticleDate date) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.textUrl = textUrl;
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

    public String getImageUrl() {
        return imageUrl.getValue();
    }

    public ArticleTextUrl getTextUrl() {
        return textUrl;
    }

    public ArticleDate getDate() {
        return date;
    }
}
