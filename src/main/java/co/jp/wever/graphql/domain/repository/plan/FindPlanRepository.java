package co.jp.wever.graphql.domain.repository.plan;

import org.springframework.stereotype.Repository;

import java.util.List;

import co.jp.wever.graphql.infrastructure.datamodel.plan.LearningPlanItemEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanElementDetailEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.aggregation.PlanItemEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.aggregation.PlanDetailEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.aggregation.PlanListItemEntity;

@Repository
public interface FindPlanRepository {
    PlanListItemEntity findOne(String planId, String userId);

    PlanDetailEntity findDetail(String planId);

    List<PlanListItemEntity> findAll(String id);

    List<PlanListItemEntity> findAllPublished(String id);

    List<PlanEntity> findAllDrafted(String id);

    List<PlanItemEntity> findAllLiked(String id);

    List<LearningPlanItemEntity> findAllLearning(String id);

    List<PlanEntity> findAllLearned(String id);

    List<String> findAllPlanElementIds(String id);

    List<PlanItemEntity> findFamous();

    List<PlanEntity> findRelated(String id);

    List<PlanElementDetailEntity> findAllPlanElementDetails(String planId, String userId);

    List<PlanElementDetailEntity> findAllPlanElementDetailsForGuest(String planId);

    String findAuthorId(String planId);
}
