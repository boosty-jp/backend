package co.jp.wever.graphql.infrastructure.datamodel.course.aggregation;

import java.util.List;

import co.jp.wever.graphql.infrastructure.datamodel.course.PlanBaseEntity;
import co.jp.wever.graphql.infrastructure.datamodel.course.PlanElementEntity;
import co.jp.wever.graphql.infrastructure.datamodel.course.PlanStatisticsEntity;
import co.jp.wever.graphql.infrastructure.datamodel.course.PlanUserActionEntity;
import co.jp.wever.graphql.infrastructure.datamodel.tag.TagEntity;
import co.jp.wever.graphql.infrastructure.datamodel.user.UserEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanDetailEntity {
    private PlanBaseEntity base;
    private List<TagEntity> tags;
    private UserEntity author;
    private List<PlanElementEntity> elementEntities;
    private PlanStatisticsEntity statistics;
    private PlanUserActionEntity actions;
}
