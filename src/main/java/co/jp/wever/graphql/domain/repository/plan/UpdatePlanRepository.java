package co.jp.wever.graphql.domain.repository.plan;

import org.springframework.stereotype.Repository;

import java.util.List;

import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanBaseEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanElementEntity;

@Repository
public interface UpdatePlanRepository {

    void updateBase(String planId, PlanBaseEntity planBaseEntity);

    void updateElements(String planId, List<PlanElementEntity> planElementEntities);

    void publishOne(String planId, String userId);

    void draftOne(String planId, String userId);

    void startOne(String planId, String userId);

    void stopOne(String planId, String userId);

    void finishOne(String planId, String userId);
}
