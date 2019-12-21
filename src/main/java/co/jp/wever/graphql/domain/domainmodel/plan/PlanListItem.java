package co.jp.wever.graphql.domain.domainmodel.plan;

import java.util.List;

import co.jp.wever.graphql.domain.domainmodel.plan.action.PlanUserAction;
import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanBase;
import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanStatus;
import co.jp.wever.graphql.domain.domainmodel.plan.statistics.PlanStatistics;
import co.jp.wever.graphql.domain.domainmodel.tag.Tag;

public class PlanListItem {
    private PlanBase base;
    private List<Tag> tags;
    private PlanUserAction action;
    private PlanStatistics statistics;

    public PlanListItem(
        PlanBase base, List<Tag> tags, PlanUserAction action, PlanStatistics statistics) {
        this.base = base;
        this.tags = tags;
        this.action = action;
        this.statistics = statistics;
    }

    public PlanBase getBase() {
        return base;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public PlanStatistics getStatistics() {
        return statistics;
    }

    public boolean published() {
        return base.getStatus().getString().equals(PlanStatus.PUBLISHED.getString());
    }

    public PlanUserAction getAction() {
        return action;
    }
}
