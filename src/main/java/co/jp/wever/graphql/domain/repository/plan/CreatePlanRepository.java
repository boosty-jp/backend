package co.jp.wever.graphql.domain.repository.plan;

import org.springframework.stereotype.Repository;

import java.util.List;

import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanBaseEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanElementEntity;

@Repository
public interface CreatePlanRepository {
    String initOne(String userId);

    String createBase(String userId, PlanBaseEntity planBaseEntity);

    void createElements(String planId, List<PlanElementEntity> planElementEntities);
}
