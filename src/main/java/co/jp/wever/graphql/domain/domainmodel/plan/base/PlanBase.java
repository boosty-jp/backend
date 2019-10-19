package co.jp.wever.graphql.domain.domainmodel.plan.base;

import java.util.List;

import co.jp.wever.graphql.domain.domainmodel.user.User;
import co.jp.wever.graphql.domain.domainmodel.user.UserId;

public class PlanBase {
    private PlanId id;
    private PlanTitle title;
    private PlanDescription description;
    private PlanImageUrl imageUrl;
    private PlanTagIds tagIds;
    private PlanStatus status;
    private UserId authorId;

    private PlanBase(
        PlanTitle title,
        PlanDescription description,
        PlanImageUrl imageUrl,
        PlanTagIds tagIds,
        UserId userId,
        PlanStatus status) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.tagIds = tagIds;
        this.status = status;
        this.authorId = userId;
    }

    public static PlanBase of(
        String title, String description, String imageUrl, List<String> tagIds, String userId, String status) {
        return new PlanBase(PlanTitle.of(title),
                            PlanDescription.of(description),
                            PlanImageUrl.of(imageUrl),
                            PlanTagIds.of(tagIds),
                            UserId.of(userId),
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

    public UserId getAuthorId() {
        return authorId;
    }

    public PlanStatus getStatus() {
        return status;
    }

    public boolean isPublished() {
        return status.equals(PlanStatus.PUBLISHED) ? true : false;
    }
}
