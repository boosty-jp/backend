package co.jp.wever.graphql.domain.domainmodel.article.base;

import java.util.List;

import co.jp.wever.graphql.domain.domainmodel.tag.TagIds;

public class PublishArticleBase {
    private ArticleId id;
    private PublishArticleTitle title;
    private ArticleImageUrl imageUrl;
    private TagIds tagIds;

    private PublishArticleBase(
        ArticleId id, PublishArticleTitle title, ArticleImageUrl imageUrl, TagIds tagIds) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.tagIds = tagIds;
    }

    public static PublishArticleBase of(String id, String title, String imageUrl, List<String> tagIds) {

        ArticleId articleId = ArticleId.of(id);
        PublishArticleTitle articleTitle = PublishArticleTitle.of(title);
        ArticleImageUrl articleImageUrl = ArticleImageUrl.of(imageUrl);
        TagIds articleTagIds = TagIds.of(tagIds);

        return new PublishArticleBase(articleId, articleTitle, articleImageUrl, articleTagIds);
    }

    public ArticleId getId() {
        return id;
    }

    public PublishArticleTitle getTitle() {
        return title;
    }

    public ArticleImageUrl getImageUrl() {
        return imageUrl;
    }

    public TagIds getTagIds() {
        return tagIds;
    }

}
