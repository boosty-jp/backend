package co.jp.wever.graphql.domain.repository.plan;

import org.springframework.stereotype.Repository;

import java.util.List;

import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanBaseEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.aggregation.PlanDetailEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.aggregation.PlanListItemEntity;

@Repository
public interface FindPlanRepository {
    PlanDetailEntity findOne(String planId);

    List<PlanListItemEntity> findAll(String id);

    List<PlanEntity> findAllPublished(String id);

    List<PlanEntity> findAllDrafted(String id);

    List<PlanEntity> findAllLiked(String id);

    List<PlanEntity> findAllLearning(String id);

    List<PlanEntity> findAllLearned(String id);

    List<PlanEntity> findFamous(String id);

    List<PlanEntity> findRelated(String id);

    PlanBaseEntity findBase(String planId);

    List<String> findPublishedPlanElementIds(List<String> ids);

    String findAuthorId(String planId);
}
