package co.jp.wever.graphql.infrastructure.repository.plan;

import org.springframework.stereotype.Component;

import co.jp.wever.graphql.domain.repository.plan.DeletePlanRepository;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanEntity;

@Component
public class DeletePlanRepositoryImpl implements DeletePlanRepository {
    @Override
    public PlanEntity deleteOne(String planId, String userId) {
        return PlanEntity.builder().build();
    }
}
