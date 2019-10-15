package co.jp.wever.graphql.domain.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

import co.jp.wever.graphql.infrastructure.datamodel.Plan;

@Repository
public interface  PlanRepository {
    //Query
    Plan findOne(String planId);
    List<Plan> findAll(String id);
    List<Plan> findAllPublished(String id);
    List<Plan> findAllDrafted(String id);
    List<Plan> findAllLiked(String id);
    List<Plan> findAllLearning(String id);
    List<Plan> findAllLearned(String id);
    List<Plan> findFamous(String id);
    List<Plan> findRelated(String id);

    //Mutation
    String initOne(String userId);
    Plan addOne(String id);
    Plan updateOne(String planId, String title, String userId);
    Plan deleteOne(String id);
    Plan publishOne(String id);
    Plan draftOne(String id);
    Plan startOne(String id);
}
