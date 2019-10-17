package co.jp.wever.graphql.domain.repository.plan;

import org.springframework.stereotype.Repository;

import co.jp.wever.graphql.infrastructure.datamodel.PlanEntity;

@Repository
public interface DeletePlanRepository {
    PlanEntity deleteOne(String planId, String userId);
}
