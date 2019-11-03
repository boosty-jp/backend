package co.jp.wever.graphql.domain.domainmodel.plan;

import java.util.List;

import co.jp.wever.graphql.domain.domainmodel.plan.base.publish.PublishPlanBase;
import co.jp.wever.graphql.domain.domainmodel.plan.element.WritePlanElement;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PublishPlanElements;

public class PublishPlan {
    private PublishPlanBase base;
    private PublishPlanElements elements;

    public PublishPlan(PublishPlanBase base, PublishPlanElements elements) {
        this.base = base;
        this.elements = elements;
    }

    public String getId() {
        return base.getId();
    }

    public String getTitle() {
        return base.getTitle();
    }

    public String getDescription() {
        return base.getDescription();
    }

    public String getImageUrl() {
        return base.getImageUrl();
    }

    public List<WritePlanElement> getElements() {
        return elements.getElements();
    }

    public List<String> getTagIds() {
        return base.getTagIds();
    }
}
