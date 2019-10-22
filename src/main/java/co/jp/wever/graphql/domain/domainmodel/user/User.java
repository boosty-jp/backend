package co.jp.wever.graphql.domain.domainmodel.user;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.domainmodel.tag.Tag;

public class User {

    private UserId userId;
    private UserDisplayName displayName;
    private UserDescription description;
    private UserImageUrl imageUrl;
    private UserUrl url;
    private List<Tag> tags;

    public User(
        UserId userId,
        UserDisplayName displayName,
        UserDescription description,
        UserImageUrl imageUrl,
        UserUrl url,
        List<Tag> tags) {
        this.userId = userId;
        this.description = description;
        this.displayName = displayName;
        this.imageUrl = imageUrl;
        this.url = url;
        this.tags = tags;
    }

    public UserId getUserId() {
        return userId;
    }

    public UserDisplayName getDisplayName() {
        return displayName;
    }

    public UserDescription getDescription() {
        return description;
    }

    public UserImageUrl getImageUrl() {
        return imageUrl;
    }

    public UserUrl getUrl() {
        return url;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public boolean hasDuplicatedTagIds() {
        List<String> tagIds = tags.stream().map(t -> t.getId().getValue()).collect(Collectors.toList());
        return tagIds.stream().filter(t -> Collections.frequency(tagIds, t) > 1).count() > 0 ? true : false;
    }
}
