package co.jp.wever.graphql.application.converter.plan;

import co.jp.wever.graphql.application.datamodel.response.query.plan.PlanBaseResponse;
import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanBase;
import co.jp.wever.graphql.infrastructure.util.DateToStringConverter;

public class PlanBaseResponseConverter {
    public static PlanBaseResponse toPlanBaseResponse(PlanBase planBase) {
        return PlanBaseResponse.builder()
                               .id(planBase.getPlanId())
                               .title(planBase.getTitle())
                               .description(planBase.getDescription())
                               .imageUrl(planBase.getImageUrl())
                               .status(planBase.getStatus().getString())
                               .createDate(DateToStringConverter.toDateString(planBase.getDate().getCreateDate()))
                               .updateDate(DateToStringConverter.toDateString(planBase.getDate().getUpdateDate()))
                               .build();
    }
}
