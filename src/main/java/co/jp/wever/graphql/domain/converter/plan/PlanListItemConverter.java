package co.jp.wever.graphql.domain.converter.plan;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.converter.tag.TagConverter;
import co.jp.wever.graphql.domain.domainmodel.plan.PlanListItem;
import co.jp.wever.graphql.domain.domainmodel.plan.action.PlanUserAction;
import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanBase;
import co.jp.wever.graphql.domain.domainmodel.plan.statistics.PlanStatistics;
import co.jp.wever.graphql.domain.domainmodel.tag.Tag;
import co.jp.wever.graphql.infrastructure.datamodel.course.aggregation.PlanListItemEntity;

public class PlanListItemConverter {
    public static PlanListItem toPlanListItem(PlanListItemEntity planListItemEntity) {
        PlanBase base = PlanBaseConverter.toPlanBase(planListItemEntity.getBase());
        List<Tag> tags =
            planListItemEntity.getTags().stream().map(t -> TagConverter.toTag(t)).collect(Collectors.toList());
        PlanUserAction action = new PlanUserAction(planListItemEntity.getActions().isLiked(),
                                                   planListItemEntity.getActions().isLearning(),
                                                   planListItemEntity.getActions().isLearned());
        PlanStatistics statistics = PlanStatisticsConverter.toPlanStatistics(planListItemEntity.getStatistics());
        return new PlanListItem(base, tags, action, statistics);
    }
}
