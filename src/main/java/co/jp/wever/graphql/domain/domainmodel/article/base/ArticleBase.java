package co.jp.wever.graphql.domain.domainmodel.article.base;

public class ArticleBase {
    private ArticleId id;
    private ArticleTitle title;
    private ArticleImageUrl imageUrl;
    private ArticleStatus status;
    private ArticleDate date;

    public ArticleBase(
        ArticleId id,
        ArticleTitle title,
        ArticleImageUrl imageUrl,
        ArticleStatus status,
        ArticleDate date) {
        this.id = id;
        this.title = title;
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

    public String getImageUrl() {
        return imageUrl.getValue();
    }

    public ArticleDate getDate() {
        return date;
    }
}
