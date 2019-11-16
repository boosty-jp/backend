package co.jp.wever.graphql.domain.domainmodel.article.base;

import java.util.List;

import co.jp.wever.graphql.domain.domainmodel.tag.TagIds;

public class DraftArticleBase {
    private ArticleId id;
    private ArticleTitle title;
    private ArticleImageUrl imageUrl;
    private TagIds tagIds;


    private DraftArticleBase(
        ArticleId id, ArticleTitle title, ArticleImageUrl imageUrl, TagIds tagIds) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.tagIds = tagIds;
    }

    public static DraftArticleBase of(String id, String title, String imageUrl, List<String> tagIds) {

        ArticleId articleId = ArticleId.of(id);
        ArticleTitle articleTitle = ArticleTitle.of(title);
        ArticleImageUrl articleImageUrl = ArticleImageUrl.of(imageUrl);
        TagIds articleTagIds = TagIds.of(tagIds);

        return new DraftArticleBase(articleId, articleTitle, articleImageUrl, articleTagIds);
    }

    public ArticleId getId() {
        return id;
    }

    public ArticleTitle getTitle() {
        return title;
    }

    public ArticleImageUrl getImageUrl() {
        return imageUrl;
    }

    public TagIds getTagIds() {
        return tagIds;
    }
}
