package co.jp.wever.graphql.domain.domainmodel.plan.base;

import java.util.Date;

public class PlanBase {
    private PlanId id;
    private PlanTitle title;
    private PlanDescription description;
    private PlanImageUrl imageUrl;
    private PlanStatus status;
    private PlanDate date;

    private PlanBase(
        PlanId id,
        PlanTitle title,
        PlanDescription description,
        PlanImageUrl imageUrl,
        PlanStatus status,
        PlanDate date) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.status = status;
        this.date = date;
    }

    public static PlanBase of(
        String id, String title, String description, String imageUrl, String status, Date createDate, Date updateDate) {
        return new PlanBase(PlanId.of(id),
                            PlanTitle.of(title),
                            PlanDescription.of(description),
                            PlanImageUrl.of(imageUrl),
                            PlanStatus.fromString(status),
                            PlanDate.of(createDate, updateDate));
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

    public String getPlanId() {
        return id.getValue();
    }

    public PlanStatus getStatus() {
        return status;
    }

    public PlanDate getDate() {
        return date;
    }

    public boolean isPublished() {
        return status.equals(PlanStatus.PUBLISHED) ? true : false;
    }
}
