package co.jp.wever.graphql.application.converter.plan;

import java.util.Date;

import co.jp.wever.graphql.application.datamodel.response.query.plan.PlanBaseResponse;
import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanBase;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanBaseEntity;
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
    public static PlanBaseResponse toPlanBaseResponse(PlanBaseEntity planBaseEntity) {
        return PlanBaseResponse.builder()
                               .id(planBaseEntity.getId())
                               .title(planBaseEntity.getTitle())
                               .description(planBaseEntity.getDescription())
                               .imageUrl(planBaseEntity.getImageUrl())
                               .status(planBaseEntity.getStatus())
                               .createDate(DateToStringConverter.toDateString(new Date(planBaseEntity.getUpdatedDate() )))
                               .updateDate(DateToStringConverter.toDateString(new Date(planBaseEntity.getUpdatedDate() )))
                               .build();
    }
}
