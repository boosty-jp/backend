package co.jp.wever.graphql.domain.domainmodel.plan;

import java.util.List;

import co.jp.wever.graphql.domain.domainmodel.user.User;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElement;

public class Plan {
    private final int MAX_PLAN_ELEMENT_COUNT = 50;
    private PlanId id;
    private PlanTitle title;
    private PlanDescription description;
    private String imageUrl;
    private List<PlanElement> elements;
    private PlanStatus status;
    private User author;

    public Plan(PlanId id, PlanTitle title, PlanDescription description, String imageUrl, List<PlanElement> elements) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.elements = elements;
    }

    public boolean canAddElement(PlanElement element) {
        if (elements.size() > MAX_PLAN_ELEMENT_COUNT - 1) {
            return false;
        }

        return element.getNumber() != elements.size() + 1 ? true : false;
    }

    public boolean isPublished() {
        return this.status.name().equals(PlanStatus.PUBLISHED.name()) ? true : false;
    }

    public User getAuthor() {
        return this.author;
    }

    public String getPlanId() {
        return id.getValue();
    }

    public String getTitle() {
        return title.getValue();
    }

    public String getDescription() {
        return description.getValue();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<PlanElement> getElements() {
        return elements;
    }

    public PlanStatus getStatus() {
        return status;
    }
}
