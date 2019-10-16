package co.jp.wever.graphql.domain.domainmodel.plan;

import java.util.List;

import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanBase;
import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanStatus;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElement;

public class Plan {
    private PlanBase base;
    private List<PlanElement> elements;

    public Plan(PlanBase planBase, List<PlanElement> elements) {
        this.base = planBase;
        this.elements = elements;
    }

    public String getAuthorId() {
        return base.getAuthor().getUserId();
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

    public List<PlanElement> getElements() {
        return elements;
    }

    public PlanStatus getStatus() {
        return base.getStatus();
    }
}
