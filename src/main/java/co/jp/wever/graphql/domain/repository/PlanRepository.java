package co.jp.wever.graphql.domain.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

import co.jp.wever.graphql.infrastructure.datamodel.PlanBaseEntity;
import co.jp.wever.graphql.infrastructure.datamodel.PlanElementEntity;
import co.jp.wever.graphql.infrastructure.datamodel.PlanEntity;

@Repository
public interface PlanRepository {
    //Query
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

    boolean isPublishedPlanElement(String elementId);

    boolean isPublishedPlan(String planId);

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
