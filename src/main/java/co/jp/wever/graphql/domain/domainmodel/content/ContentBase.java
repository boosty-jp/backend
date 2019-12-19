package co.jp.wever.graphql.domain.domainmodel.content;

import java.util.List;

public class ContentBase {
    private ContentTitle title;
    private ContentImageUrl imageUrl;
    private ContentTagIds tagIds;
    private boolean entry;

    private ContentBase(
        ContentTitle title, ContentImageUrl imageUrl, ContentTagIds tagIds, boolean entry) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.tagIds = tagIds;
        this.entry = entry;
    }

    public static ContentBase of(String title, String imageUrl, List<String> tagIds, boolean entry) {
        return new ContentBase(ContentTitle.of(title), ContentImageUrl.of(imageUrl), ContentTagIds.of(tagIds), entry);
    }

    public ContentTitle getTitle() {
        return title;
    }

    public ContentImageUrl getImageUrl() {
        return imageUrl;
    }

    public ContentTagIds getTagIds() {
        return tagIds;
    }

    public boolean isEntry() {
        return entry;
    }

    public boolean valid() {
        return title.valid();
    }
}
