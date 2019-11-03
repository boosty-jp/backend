package co.jp.wever.graphql.domain.domainmodel.plan;

import java.util.List;

import co.jp.wever.graphql.domain.domainmodel.plan.base.draft.DraftPlanBase;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElement;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElements;

public class DraftPlan {
    private DraftPlanBase base;
    private PlanElements elements;

    public DraftPlan(DraftPlanBase base, PlanElements elements) {
        this.base = base;
        this.elements = elements;
    }

    public String getId() {
        return base.getId();
    }

    public String getTitle() {
        return base.getId();
    }

    public String getDescription() {
        return base.getDescription();
    }

    public String getImageUrl() {
        return base.getImageUrl();
    }

    public List<PlanElement> getElements() {
        return elements.getElements();
    }

    public List<String> getTagIds() {
        return base.getTags();
    }
}
