package co.jp.wever.graphql.domain.domainmodel.plan.base;

import java.util.List;

import co.jp.wever.graphql.domain.domainmodel.user.User;

public class PlanBase {
    private PlanId id;
    private PlanTitle title;
    private PlanDescription description;
    private PlanImageUrl imageUrl;
    private PlanTagIds tagIds;
    private User author;
    private PlanStatus status;

    private PlanBase(
        PlanTitle title,
        PlanDescription description,
        PlanImageUrl imageUrl,
        PlanTagIds tagIds,
        User user,
        PlanStatus status) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.tagIds = tagIds;
        this.status = status;
        this.author = user;
    }

    public static PlanBase of(
        String title, String description, String imageUrl, List<String> tagIds, String userId, String status) {
        return new PlanBase(PlanTitle.of(title),
                            PlanDescription.of(description),
                            PlanImageUrl.of(imageUrl),
                            PlanTagIds.of(tagIds),
                            User.of(userId),
                            PlanStatus.valueOf(status));
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

    public List<String> getTagIds() {
        return tagIds.getValue();
    }

    public String getPlanId() {
        return id.getValue();
    }

    public User getAuthor() {
        return author;
    }

    public PlanStatus getStatus() {
        return status;
    }

    public boolean isPublished() {
        return status.equals(PlanStatus.PUBLISHED) ? true : false;
    }
}
