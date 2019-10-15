package co.jp.wever.graphql.domain.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

import co.jp.wever.graphql.infrastructure.datamodel.PlanElementEntity;
import co.jp.wever.graphql.infrastructure.datamodel.PlanEntity;

@Repository
public interface  PlanRepository {
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
    boolean isPublishedPlanElement(String elementId);

    //Mutation
    String initOne(String userId);
    String addOne(String planId, String userId, PlanElementEntity planElementEntity);
    PlanEntity updateOne(String planId, String title, String userId);
    PlanEntity deleteOne(String id);
    PlanEntity publishOne(String id);
    PlanEntity draftOne(String id);
    PlanEntity startOne(String id);
}
