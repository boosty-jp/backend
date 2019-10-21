package co.jp.wever.graphql.domain.repository.plan;

import org.springframework.stereotype.Repository;

import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanEntity;

@Repository
public interface DeletePlanRepository {
    void deleteOne(String planId, String userId);
}
