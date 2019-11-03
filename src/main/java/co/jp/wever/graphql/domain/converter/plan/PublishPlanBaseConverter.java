package co.jp.wever.graphql.domain.converter.plan;

import co.jp.wever.graphql.application.datamodel.request.PlanBaseInput;
import co.jp.wever.graphql.domain.domainmodel.plan.base.publish.PublishPlanBase;

public class PublishPlanBaseConverter {
    public static PublishPlanBase toPublishPlanBase(PlanBaseInput planBaseInput) {
        return PublishPlanBase.of(planBaseInput.getId(),
                                  planBaseInput.getTitle(),
                                  planBaseInput.getDescription(),
                                  planBaseInput.getImageUrl(),
                                  planBaseInput.getTags());
    }
}
