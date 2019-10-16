package co.jp.wever.graphql.domain.repository.plan;

import org.springframework.stereotype.Repository;

import java.util.List;

import co.jp.wever.graphql.infrastructure.datamodel.PlanBaseEntity;
import co.jp.wever.graphql.infrastructure.datamodel.PlanElementEntity;
import co.jp.wever.graphql.infrastructure.datamodel.PlanEntity;

@Repository
public interface PlanRepository {
    //Mutation

    String createBase(String userId, PlanBaseEntity planBaseEntity);

    void updateBase(String planId, PlanBaseEntity planBaseEntity);

    void createElements(String planId, List<PlanElementEntity> planElementEntities);

    void updateElements(String planId, List<PlanElementEntity> planElementEntities);

    PlanEntity deleteOne(String planId, String userId);

    PlanEntity publishOne(String planId, String userId);

    PlanEntity draftOne(String planId, String userId);

    PlanEntity startOne(String planId, String userId);
}
