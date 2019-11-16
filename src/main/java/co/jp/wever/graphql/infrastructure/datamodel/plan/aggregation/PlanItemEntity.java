package co.jp.wever.graphql.infrastructure.datamodel.plan.aggregation;

import java.util.List;

import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanBaseEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanStatisticsEntity;
import co.jp.wever.graphql.infrastructure.datamodel.tag.TagEntity;
import co.jp.wever.graphql.infrastructure.datamodel.user.UserEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanItemEntity {
    private PlanBaseEntity base;
    private List<TagEntity> tags;
    private UserEntity author;
    private long elementCount;
    private PlanStatisticsEntity statistics;
}
