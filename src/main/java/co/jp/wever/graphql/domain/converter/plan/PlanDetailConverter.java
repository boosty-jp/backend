package co.jp.wever.graphql.domain.converter.plan;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.converter.tag.TagConverter;
import co.jp.wever.graphql.domain.converter.user.UserConverter;
import co.jp.wever.graphql.domain.domainmodel.plan.PlanDetail;
import co.jp.wever.graphql.domain.domainmodel.plan.action.PlanUserAction;
import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanBase;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElement;
import co.jp.wever.graphql.domain.domainmodel.plan.statistics.PlanStatistics;
import co.jp.wever.graphql.domain.domainmodel.tag.Tag;
import co.jp.wever.graphql.domain.domainmodel.user.User;
import co.jp.wever.graphql.infrastructure.datamodel.plan.aggregation.PlanDetailEntity;

public class PlanDetailConverter {
    public static PlanDetail toPlanDetail(PlanDetailEntity planDetailEntity) {
        PlanBase base = PlanBaseConverter.toPlanBase(planDetailEntity.getBase());
        List<Tag> tags =
            planDetailEntity.getTags().stream().map(t -> TagConverter.toTag(t)).collect(Collectors.toList());
        User author = UserConverter.toUser(planDetailEntity.getAuthor());
        List<PlanElement> elements = planDetailEntity.getElementEntities()
                                                     .stream()
                                                     .map(e -> PlanElementConverter.toPlanElement(e))
                                                     .collect(Collectors.toList());
        PlanStatistics statistics = PlanStatisticsConverter.toPlanStatistics(planDetailEntity.getStatistics());
        PlanUserAction action = PlanUserActionConverter.toPlanUserAction(planDetailEntity.getActions());
        return PlanDetail.of(base, tags, author, elements, statistics, action);
    }
}
