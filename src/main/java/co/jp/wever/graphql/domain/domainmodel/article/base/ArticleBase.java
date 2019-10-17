package co.jp.wever.graphql.domain.domainmodel.article.base;

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
}
