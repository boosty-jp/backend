package co.jp.wever.graphql.infrastructure.datamodel.plan.aggregation;

import java.util.List;

import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanBaseEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanStatisticsEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanUserActionEntity;
import co.jp.wever.graphql.infrastructure.datamodel.tag.TagEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanListItemEntity {
    private PlanBaseEntity base;
    private List<TagEntity> tags;
    private PlanUserActionEntity actions;
    private PlanStatisticsEntity statistics;
}
