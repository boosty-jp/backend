package co.jp.wever.graphql.domain.repository.plan;

import org.springframework.stereotype.Repository;

import java.util.List;

import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanBaseEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanElementDetailEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.LearningPlanItemEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.aggregation.FamousPlanEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.aggregation.PlanDetailEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.aggregation.PlanListItemEntity;

@Repository
public interface FindPlanRepository {
    PlanListItemEntity findOne(String planId, String userId);

    PlanDetailEntity findDetail(String planId);

    List<PlanListItemEntity> findAll(String id);

    List<PlanListItemEntity> findAllPublished(String id);

    List<PlanEntity> findAllDrafted(String id);

    List<PlanEntity> findAllLiked(String id);

    List<LearningPlanItemEntity> findAllLearning(String id);

    List<PlanEntity> findAllLearned(String id);

    List<FamousPlanEntity> findFamous();

    List<PlanEntity> findRelated(String id);

    PlanBaseEntity findBase(String planId);

    List<String> findPublishedPlanElementIds(List<String> ids);

    List<PlanElementDetailEntity> findAllPlanElementDetails(String planId, String userId);

    String findAuthorId(String planId);
}
