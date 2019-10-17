package co.jp.wever.graphql.domain.repository.plan;

import org.springframework.stereotype.Repository;

import java.util.List;

import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanBaseEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanEntity;

@Repository
public interface FindPlanRepository {
    PlanEntity findOne(String planId);

    List<PlanEntity> findAll(String id);

    List<PlanEntity> findAllPublished(String id);

    List<PlanEntity> findAllDrafted(String id);

    List<PlanEntity> findAllLiked(String id);

    List<PlanEntity> findAllLearning(String id);

    List<PlanEntity> findAllLearned(String id);

    List<PlanEntity> findFamous(String id);

    List<PlanEntity> findRelated(String id);

    PlanBaseEntity findBase(String planId);

    List<String> findPublishedPlanElementIds(List<String> ids);

}
