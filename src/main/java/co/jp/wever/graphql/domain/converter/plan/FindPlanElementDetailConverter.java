package co.jp.wever.graphql.domain.converter.plan;

import co.jp.wever.graphql.domain.domainmodel.plan.element.FindPlanElementAction;
import co.jp.wever.graphql.domain.domainmodel.plan.element.FindPlanElementDetail;
import co.jp.wever.graphql.domain.domainmodel.plan.element.FindPlanElementStatistics;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElementId;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElementImageUrl;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElementNumber;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElementTitle;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElementType;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanElementDetailEntity;

public class FindPlanElementDetailConverter {
    public static FindPlanElementDetail toFindPlanElementDetail(PlanElementDetailEntity planElementDetailEntity) {
        PlanElementId id = PlanElementId.of(planElementDetailEntity.getId());
        PlanElementTitle title = PlanElementTitle.of(planElementDetailEntity.getTitle());
        PlanElementImageUrl imageUrl = PlanElementImageUrl.of(planElementDetailEntity.getImageUrl());
        PlanElementType type = PlanElementType.fromString(planElementDetailEntity.getType());
        PlanElementNumber number = PlanElementNumber.of(planElementDetailEntity.getNumber());
        FindPlanElementStatistics statistics =
            new FindPlanElementStatistics(planElementDetailEntity.getStatistics().getLike(),
                                          planElementDetailEntity.getStatistics().getLearned());
        FindPlanElementAction action = new FindPlanElementAction(planElementDetailEntity.getAction().isLiked(),
                                                                 planElementDetailEntity.getAction().isLearned());

        return new FindPlanElementDetail(id, title, imageUrl, type, number, statistics, action);
    }
}
