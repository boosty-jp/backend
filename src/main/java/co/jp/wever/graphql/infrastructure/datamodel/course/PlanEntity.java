package co.jp.wever.graphql.infrastructure.datamodel.course;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanEntity {
    private String id;
    private PlanBaseEntity baseEntity;
    private List<PlanElementEntity> elementEntities;
}
